package com.egemert.swapcardcase.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {

        userListState.let { safeState ->
            when (safeState) {
                is UserListUiState.Loading -> {

                }

                is UserListUiState.Success -> {
                    val userList = safeState.users.toMutableStateList()
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                    ) {
                        Text(text = userList.first().email.orEmpty())
                    }
                }

                is UserListUiState.Error -> {
                    //todo:Error page
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Error-->${safeState.message}",
                            fontSize = 32.sp,
                            color = Color.Red
                        )
                    }
                }

                is UserListUiState.Exception -> {
                    //todo:Exception page
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Exception-->\n${safeState.throwable}",
                            fontSize = 32.sp,
                            color = Color.Red,
                        )
                    }
                }

                UserListUiState.Initial -> {}
                else -> {}
            }
        }
    }
}