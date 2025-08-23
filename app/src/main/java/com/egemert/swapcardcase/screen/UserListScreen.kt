package com.egemert.swapcardcase.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.egemert.swapcardcase.viewmodel.UserListUiState
import com.egemert.swapcardcase.viewmodel.UserListViewModel

@Composable
fun UserListScreen(modifier: Modifier = Modifier) {
    val userListViewModel: UserListViewModel = hiltViewModel()
    val userListState by userListViewModel.userListState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        userListViewModel.getUserList()
    }

    Box(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        when (val state = userListState) {
            is UserListUiState.Loading -> {
                CircularProgressIndicator()
            }

            is UserListUiState.Success -> {
                if (state.users.isEmpty()) {
                    Text("No users found")
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(state.users) { user ->
                            Text(
                                text = user.email ?: "No email",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillParentMaxWidth(),
                                textAlign = TextAlign.Center
                            )
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

            UserListUiState.Initial -> {}
            else -> {}
        }
    }
}
