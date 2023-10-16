package com.example.bankapp.presentation.state

data class SetUsersState (
    val isLoading: Boolean = false,
    val setUsers: Int? = null,
    val error: String? = null
)