package com.example.bankapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankapp.common.Resources
import com.example.bankapp.data.local.entity.setUsers
import com.example.bankapp.domain.usecase.UseCases
import com.example.bankapp.presentation.state.GetUserState
import com.example.bankapp.presentation.state.GetUsersState
import com.example.bankapp.presentation.state.InsertRoomResultState
import com.example.bankapp.presentation.state.SetUsersState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val useCases: UseCases
) : ViewModel() {

    private val _getUser = MutableStateFlow(GetUserState())
    val getUser: StateFlow<GetUserState> = _getUser.asStateFlow()

    private val _getUsers = MutableStateFlow(GetUsersState())
    val getUsersList: StateFlow<GetUsersState> = _getUsers.asStateFlow()

    private val _setUsersState = MutableStateFlow(SetUsersState())
    val setUsersState: StateFlow<SetUsersState> = _setUsersState.asStateFlow()

    val _clearDataStore = MutableStateFlow("")
    val clearDataStore: StateFlow<String> = _clearDataStore

    private var getUserJob: Job? = null
    private var setUsersJob: Job? = null
    private var getUsersJob: Job? = null

    fun getUserData(email: String) {
        getUserJob?.cancel()

        getUserJob = useCases.getUserDetailUseCase(email).onEach { result ->
            when (result) {
                is Resources.Loading -> {
                    _getUser.update {
                        it.copy(isLoading = true)
                    }
                    _getUser.update {
                        it.copy(error = null)
                    }
                }

                is Resources.Error -> {
                    _getUser.update {
                        it.copy(isLoading = false)
                    }
                    _getUser.update {
                        it.copy(error = result.message)
                    }
                }

                is Resources.Success -> {
                    _getUser.update {
                        it.copy(isLoading = false)
                    }
                    _getUser.update {
                        it.copy(error = null)
                    }

                    _getUser.update {
                        it.copy(userData = result.data)
                    }
                }

                else -> Unit
            }
        }.launchIn(viewModelScope)
    }

    suspend fun getAllUsersList() {
        getUsersJob = useCases.getUsersListUseCase().onEach { result ->
            when (result) {
                is Resources.Loading -> {
                    _getUsers.update {
                        it.copy(isLoading = true)
                    }
                    _getUsers.update {
                        it.copy(error = null)
                    }
                }

                is Resources.Error -> {
                    _getUsers.update {
                        it.copy(isLoading = false)
                    }
                    _getUsers.update {
                        it.copy(error = result.message)
                    }
                }

                is Resources.Success -> {
                    _getUsers.update {
                        it.copy(isLoading = false)
                    }
                    _getUsers.update {
                        it.copy(error = null)
                    }

                    _getUsers.update {
                        it.copy(usersList = result.data)
                    }
                }

                else -> Unit
            }
        }.launchIn(viewModelScope)
    }

    suspend fun InsertAllUsers() {
        setUsersJob = useCases.setUsersUseCase(setUsers()).onEach { result ->

            when (result) {
                is InsertRoomResultState.Error -> {
                    _setUsersState.update { it.copy(isLoading = false) }
                    _setUsersState.update { it.copy(error = result.message) }
                }

                is InsertRoomResultState.Loading -> {
                    _setUsersState.update { it.copy(isLoading = true) }
                }

                is InsertRoomResultState.Success -> {
                    _setUsersState.update { it.copy(isLoading = false) }
                    _setUsersState.update { it.copy(error = null) }
                    _setUsersState.update { it.copy(setUsers = result.data) }
                }

                else -> Unit
            }
        }.launchIn(viewModelScope)
    }

    suspend fun clearDataStore() {
        viewModelScope.launch(Dispatchers.IO) {
            _clearDataStore.value =
                useCases.clearDataStoreUseCase().toString()
        }
    }

}