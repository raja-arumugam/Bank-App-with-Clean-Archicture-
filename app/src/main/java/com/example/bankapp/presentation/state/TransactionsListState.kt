package com.example.bankapp.presentation.state

import com.example.bankapp.data.local.entity.TransactionModel
import com.example.bankapp.data.local.entity.UsersModel
import kotlinx.coroutines.flow.Flow

data class TransactionsListState(
    val isLoading: Boolean = false,
    val transaction: Flow<List<TransactionModel>>? = null,
    val error: String? = null
)