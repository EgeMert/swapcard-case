package com.egemert.swapcardcase.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import kotlinx.coroutines.runBlocking
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.egemert.swapcardcase.component.UserCardItem
import com.egemert.swapcardcase.navigation.Router
import com.egemert.swapcardcase.viewmodel.UserListUiState
import com.egemert.swapcardcase.viewmodel.UserListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(
    modifier: Modifier = Modifier,
    router: Router
) {
    val userListViewModel: UserListViewModel = hiltViewModel()
    val userListState by userListViewModel.userListState.collectAsStateWithLifecycle()
    val lazyListState = rememberLazyListState()

    // Refresh bookmarked users when the screen becomes visible
    LaunchedEffect(Unit) {
        userListViewModel.loadBookmarkedUsers()
    }

    val pullToRefreshState = rememberPullToRefreshState()

    LaunchedEffect(pullToRefreshState.isRefreshing) {
        if (pullToRefreshState.isRefreshing) {
            userListViewModel.refreshUsers()
            pullToRefreshState.endRefresh()
        }
    }

    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo }
            .collect { visibleItems ->
                if (visibleItems.isNotEmpty()) {
                    val lastVisibleItemIndex = visibleItems.last().index
                    val totalItems = lazyListState.layoutInfo.totalItemsCount

                    if (lastVisibleItemIndex >= totalItems - 3) {
                        userListViewModel.getUsers()
                    }
                }
            }
    }

    Column(
        modifier = modifier
            .nestedScroll(pullToRefreshState.nestedScrollConnection)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Users",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Favorites",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(28.dp)
                    .clickable {
                        router.goToBookmarkScreen()
                    }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(pullToRefreshState.nestedScrollConnection),
            contentAlignment = Alignment.Center
        ) {
            if (pullToRefreshState.isRefreshing) {
                PullToRefreshContainer(
                    state = pullToRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
            when (val state = userListState) {
                is UserListUiState.Loading -> {
                    CircularProgressIndicator()
                }

                is UserListUiState.Success -> {
                    if (state.users.isEmpty()) {
                        Text("No users found")
                    } else {
                        LazyColumn(
                            state = lazyListState,
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            items(
                                items = state.users,
                                key = { user ->
                                    user.login?.uuid
                                        ?: (user.name?.title + user.name?.first + user.name?.last)
                                }
                            ) { user ->
                                UserCardItem(
                                    user = user,
                                    onBookmarkClick = { isBookmarked ->
                                        userListViewModel.toggleBookmark(user)
                                    },
                                    isBookmarked = remember(user.login?.uuid) {
                                        userListViewModel.isBookmarked(user.login?.uuid.orEmpty())
                                    }
                                )
                            }

                            item {
                                if (state.isLoadingMore) {
                                    Box(
                                        modifier = Modifier
                                            .fillParentMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                } else {
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                        }
                    }
                }

                is UserListUiState.Error -> {
                    Text(
                        text = state.message ?: "An unknown error occurred",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }

                else -> {}
            }
        }
    }
}
