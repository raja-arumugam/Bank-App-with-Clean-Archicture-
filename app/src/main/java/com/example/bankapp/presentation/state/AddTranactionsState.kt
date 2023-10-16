package com.example.bankapp.presentation.state

data class AddTranactionsState (
    val isLoading: Boolean = false,
    val addTransactions: Int? = null,
    val error: String? = null
)