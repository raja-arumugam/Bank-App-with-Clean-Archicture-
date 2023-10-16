package com.example.bankapp.domain.usecase.depositUseCase

import com.example.bankapp.domain.repository.BankRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class DepositUseCase @Inject constructor(
    private val repository: BankRepository
) {
    suspend operator fun invoke(
        currentAmount: MutableStateFlow<Int>,
        amount: Int,
        email: String
    ): Int {
        val newAmount = currentAmount.value + amount
        return repository.updateAmount(newAmount.toString(), email)
    }
}