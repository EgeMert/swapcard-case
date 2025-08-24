package com.egemert.swapcardcase.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.egemert.swapcardcase.component.BasicAsyncImage
import com.egemert.swapcardcase.component.UserCardItem
import com.egemert.swapcardcase.data.response.User
import com.egemert.swapcardcase.navigation.Router
import com.egemert.swapcardcase.viewmodel.BookmarkUiState
import com.egemert.swapcardcase.viewmodel.BookmarkViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkScreen(
    modifier: Modifier = Modifier,
    router: Router
) {

    val viewModel: BookmarkViewModel = hiltViewModel()
    val bookmarkState by viewModel.bookmarkState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bookmarks") },
                navigationIcon = {
                    IconButton(onClick = { router.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        when (val state = bookmarkState) {
            is BookmarkUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is BookmarkUiState.Success -> {
                if (state.users.isEmpty()) {
                    EmptyBookmarks(
                        modifier = Modifier.padding(padding)
                    )
                } else {
                    BookmarkedUsersList(
                        users = state.users,
                        onRemoveBookmark = { user -> viewModel.removeBookmark(user) },
                        modifier = Modifier.padding(padding)
                    )
                }
            }

            is BookmarkUiState.Error -> {
                ErrorState(
                    message = state.message,
                    modifier = Modifier.padding(padding)
                )
            }

            BookmarkUiState.Empty -> {
                EmptyBookmarks(
                    modifier = Modifier.padding(padding)
                )
            }
        }
    }
}

@Composable
private fun BookmarkedUsersList(
    users: List<User>,
    onRemoveBookmark: (User) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(users, key = { it.login?.uuid ?: it.email.orEmpty() }) { user ->
            BookmarkedUserItem(
                user = user,
                onRemoveBookmark = { onRemoveBookmark(user) }
            )
        }
    }
}

@Composable
private fun BookmarkedUserItem(
    user: User,
    onRemoveBookmark: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicAsyncImage(
                image = user.picture?.large.orEmpty(),
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(30.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                if (user.name?.first.isNullOrEmpty().not() || user.name?.last.isNullOrEmpty()
                        .not()
                ) {
                    Text(
                        text = "${user.name?.first.orEmpty()} ${user.name?.last.orEmpty()}".trim(),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                }

                user.location?.country?.let { safeCountry ->
                    Text(
                        text = safeCountry,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                user.email?.let { safeEmail ->
                    Text(
                        text = safeEmail,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

            }

            IconButton(onClick = onRemoveBookmark) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove from bookmarks",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun EmptyBookmarks(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No bookmarks yet",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Bookmark users to see them here",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ErrorState(
    message: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Error loading bookmarks",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}