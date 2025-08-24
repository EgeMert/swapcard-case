package com.egemert.swapcardcase.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egemert.swapcardcase.data.response.User
import com.egemert.swapcardcase.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class BookmarkUiState {
    data object Loading : BookmarkUiState()
    data class Success(val users: List<User>) : BookmarkUiState()
    data class Error(val message: String) : BookmarkUiState()
    data object Empty : BookmarkUiState()
}

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _bookmarkState = MutableStateFlow<BookmarkUiState>(BookmarkUiState.Loading)
    val bookmarkState: StateFlow<BookmarkUiState> = _bookmarkState.asStateFlow()

    init {
        loadBookmarkedUsers()
    }

    fun loadBookmarkedUsers() {
        viewModelScope.launch {
            try {
                userRepository.getBookmarkedUsers().collectLatest { users ->
                    _bookmarkState.emit(
                        if (users.isNotEmpty()) {
                            BookmarkUiState.Success(users)
                        } else {
                            BookmarkUiState.Empty
                        }
                    )
                }
            } catch (e: Exception) {
                _bookmarkState.emit(BookmarkUiState.Error(e.message ?: "An error occurred"))
            }
        }
    }

    fun removeBookmark(user: User) {
        viewModelScope.launch {
            userRepository.removeBookmark(user)
        }
    }
}
