package com.example.bankapp.domain.usecase.debitUseCase

import com.example.bankapp.domain.repository.BankRepository
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class DebitUseCase @Inject constructor(
    private val repository: BankRepository
) {
    suspend operator fun invoke(
        currentUserAmount: MutableStateFlow<Int>,
        enteredAmount: Int,
        email: String
    ): Int {
        val newAmount = currentUserAmount.value - enteredAmount
        return repository.updateAmount(newAmount.toString(), email)
    }
}