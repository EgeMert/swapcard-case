package com.egemert.swapcardcase.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egemert.swapcardcase.network.model.User
import com.egemert.swapcardcase.network.onError
import com.egemert.swapcardcase.network.onException
import com.egemert.swapcardcase.network.onSuccess
import com.egemert.swapcardcase.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _userListState: MutableStateFlow<UserListUiState> =
        MutableStateFlow(UserListUiState.Initial)
    val userListState: StateFlow<UserListUiState?> get() = _userListState

    fun getUserList() {
        viewModelScope.launch {
            _userListState.emit(UserListUiState.Loading)
            userRepository.getUsers().onSuccess { result ->
                if (result.results == null) {
                    _userListState.emit(UserListUiState.Initial)
                    return@onSuccess
                } else {
                    _userListState.emit(UserListUiState.Success(result.results))
                }

            }.onError { message ->
                _userListState.emit(UserListUiState.Error(message))
            }.onException { e ->
                _userListState.emit(UserListUiState.Exception(e))
            }
        }
    }
}

sealed class UserListUiState {
    data object Loading : UserListUiState()
    data object Initial : UserListUiState()
    data class Success(val users: List<User>) : UserListUiState()
    data class Error(val message: String?) : UserListUiState()
    class Exception(
        val throwable: Throwable,
    ) : UserListUiState()
}