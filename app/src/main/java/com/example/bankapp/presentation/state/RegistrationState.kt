package com.example.bankapp.presentation.state


data class RegistrationState (
    val isLoading: Boolean = false,
    val userRegistration: Int? = null,
    val error: String? = null
)