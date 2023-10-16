package com.example.bankapp.presentation.state

import com.example.bankapp.data.local.entity.UsersModel
import kotlinx.coroutines.flow.Flow

data class SelectedUserState(
    val isLoading: Boolean = false,
    val user: Flow<UsersModel>? = null,
    val error: String? = null
)