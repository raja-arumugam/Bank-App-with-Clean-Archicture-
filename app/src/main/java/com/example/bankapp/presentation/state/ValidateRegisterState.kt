package com.example.bankapp.presentation.state

data class ValidateRegisterState(
    val nameInput:String = "",
    val emailInput:String = "",
    val passwordInput:String = "",
    val confirmPasswordInput:String = "",
    val bankNameInput:String = "",
    val ifscCodeInput:String = "",
    val accountNumberInput:String = "",
    val amountInput:String = "",
    val isInputValid:Boolean = false,
    val errorMessageInput:String? = null
)
