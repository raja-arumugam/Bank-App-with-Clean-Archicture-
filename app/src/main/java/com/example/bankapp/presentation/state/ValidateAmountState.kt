package com.example.bankapp.presentation.state

data class ValidateAmountState(
    val amountInput:String = "",
    val isInputValid:Boolean = false,
    val errorMessageInput:String? = null
)
