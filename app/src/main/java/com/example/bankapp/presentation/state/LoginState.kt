package com.example.bankapp.presentation.state

import com.example.bankapp.data.local.entity.RegistrationModel
import kotlinx.coroutines.flow.Flow

sealed class LoginState {
    object Loading : LoginState()
    object Empty : LoginState()
    data class Error(val throwable: Throwable) : LoginState()
    data class Success(val data: RegistrationModel) : LoginState()
}

/*
data class LoginState(
    val isLoading: Boolean = false,
    val userLogin: Flow<RegistrationModel>? = null,
    val error: String? = null,
)*/
