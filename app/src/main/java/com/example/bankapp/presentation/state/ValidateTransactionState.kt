package com.example.bankapp.presentation.state

data class ValidateTransactionState(
    val amountInput: String = "",
    val notesInput: String = "",
    val isInputValid: Boolean = false,
    val errorMessageInput: String? = null
)
