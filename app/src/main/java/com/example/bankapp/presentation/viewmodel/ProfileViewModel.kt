package com.example.bankapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankapp.common.Resources
import com.example.bankapp.domain.usecase.UseCases
import com.example.bankapp.presentation.state.GetUserState
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

class ProfileViewModel @Inject constructor(
    private val useCases: UseCases
) : ViewModel() {

    private val _getUser = MutableStateFlow(GetUserState())
    val getUser: StateFlow<GetUserState> = _getUser.asStateFlow()

    val _loggedInEmail = MutableStateFlow("")
    val loggedInEmail: StateFlow<String> = _loggedInEmail

    private var getUserJob: Job? = null

    suspend fun getLoggedInEmail() {
        viewModelScope.launch(Dispatchers.IO) {
            _loggedInEmail.value =
                useCases.readLoggedInEmailUseCase().stateIn(viewModelScope).value
        }
    }

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

}