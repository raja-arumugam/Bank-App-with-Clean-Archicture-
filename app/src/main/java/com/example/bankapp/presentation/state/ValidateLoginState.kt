package com.example.bankapp.presentation.state

data class ValidateLoginState(
    val emailInput:String = "",
    val passwordInput:String = "",
    val isInputValid:Boolean = false,
    val errorMessageInput:String? = null
)
