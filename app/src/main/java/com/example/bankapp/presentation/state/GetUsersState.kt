package com.example.bankapp.presentation.state

import com.example.bankapp.data.local.entity.UsersModel
import kotlinx.coroutines.flow.Flow

data class GetUsersState(
    val isLoading: Boolean = false,
    val usersList: Flow<List<UsersModel>>? = null,
    val error: String? = null
)