package com.example.bankapp.domain.usecase.getTransactionsUseCase

import com.example.bankapp.common.Resources
import com.example.bankapp.data.local.entity.TransactionModel
import com.example.bankapp.domain.repository.BankRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTransactionListUseCase @Inject constructor(private val repository: BankRepository) {
    suspend operator fun invoke(): Flow<Resources<List<TransactionModel>>> =
        flow {
            try {
                emit(Resources.Loading())
                val transactionList = repository.getTransactionList()
                emit(Resources.Success(transactionList))
            } catch (e: Exception) {
                emit(Resources.Error(e.localizedMessage!!))
            }
        }
}