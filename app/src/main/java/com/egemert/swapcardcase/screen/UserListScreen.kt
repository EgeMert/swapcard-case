package com.egemert.swapcardcase.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.egemert.swapcardcase.viewmodel.UserListUiState
import com.egemert.swapcardcase.viewmodel.UserListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(modifier: Modifier = Modifier) {
    val userListViewModel: UserListViewModel = hiltViewModel()
    val userListState by userListViewModel.userListState.collectAsStateWithLifecycle()
    val lazyListState = rememberLazyListState()

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
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
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(
                            items = state.users,
                            key = { user ->
                                user.login?.uuid
                                    ?: (user.name?.title + user.name?.first + user.name?.last)
                            }
                        ) { user ->
                            Text(
                                text = user.email ?: "No email",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillParentMaxWidth(),
                                textAlign = TextAlign.Center
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
