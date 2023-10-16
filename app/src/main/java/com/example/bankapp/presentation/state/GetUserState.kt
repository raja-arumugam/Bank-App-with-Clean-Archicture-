package com.example.bankapp.presentation.state

import com.example.bankapp.data.local.entity.RegistrationModel
import kotlinx.coroutines.flow.Flow

data class GetUserState(
    val isLoading: Boolean = false,
    val userData: Flow<RegistrationModel>? = null,
    val error: String? = null
)