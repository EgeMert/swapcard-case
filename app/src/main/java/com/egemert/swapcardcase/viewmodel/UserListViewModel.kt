package com.egemert.swapcardcase.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egemert.swapcardcase.data.response.User
import com.egemert.swapcardcase.network.onError
import com.egemert.swapcardcase.network.onException
import com.egemert.swapcardcase.network.onSuccess
import com.egemert.swapcardcase.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _userListState = MutableStateFlow<UserListUiState>(UserListUiState.Loading)
    val userListState: StateFlow<UserListUiState> = _userListState.asStateFlow()

    private var currentPage = 1
    private var isLoading = false
    private var isLastPage = false

    private var bookmarkedUserList = mutableListOf<User>()

    init {
        getUsers()
        loadBookmarkedUsers()
    }

    fun loadBookmarkedUsers() {
        viewModelScope.launch {
            try {
                userRepository.getBookmarkedUsers().collectLatest { users ->
                    if (users.isNotEmpty()) {
                        bookmarkedUserList = users.toMutableList()
                    }else{
                        bookmarkedUserList = emptyList<User>().toMutableList()
                    }
                }
            } catch (_: Exception) {

            }
        }
    }

    fun refreshUsers() {
        currentPage = 1
        isLastPage = false
        getUsers()
    }

    fun isBookmarked(uuid: String): Boolean {
        return bookmarkedUserList.any { user ->
            user.login?.uuid == uuid
        }
    }

    fun toggleBookmark(user: User) {
        viewModelScope.launch {
            val uuid = user.login?.uuid ?: return@launch
            
            if (isBookmarked(uuid)) {
                userRepository.removeBookmark(user)
            } else {
                userRepository.addBookmark(user)
            }
        }
    }

    fun getUsers() {
        if (isLoading || isLastPage) return

        viewModelScope.launch {
            try {
                isLoading = true

                if (currentPage == 1) {
                    _userListState.emit(UserListUiState.Loading)
                } else {
                    val currentUsers =
                        (_userListState.value as? UserListUiState.Success)?.users ?: emptyList()
                    _userListState.emit(UserListUiState.Success(currentUsers, isLoadingMore = true))
                }

                val result = userRepository.getUsers(page = currentPage)

                result.onSuccess { response ->
                    val currentUsers =
                        (_userListState.value as? UserListUiState.Success)?.users?.toMutableList()
                            ?: mutableListOf()
                    val newUsers = response.results ?: emptyList()

                    isLastPage = newUsers.isEmpty()

                    if (newUsers.isNotEmpty()) {
                        currentUsers.addAll(newUsers)
                        _userListState.emit(UserListUiState.Success(currentUsers))
                        currentPage++
                    } else if (currentPage == 1) {
                        _userListState.emit(UserListUiState.Success(emptyList()))
                    }
                }.onError { message ->
                    _userListState.emit(UserListUiState.Error(message))
                }.onException { e ->
                    _userListState.emit(UserListUiState.Exception(e))
                }
            } finally {
                isLoading = false
            }
        }
    }
}

sealed class UserListUiState {
    data object Loading : UserListUiState()
    data class Success(val users: List<User>, val isLoadingMore: Boolean = false) :
        UserListUiState()

    data class Error(val message: String?) : UserListUiState()
    data class Exception(val throwable: Throwable) : UserListUiState()
}