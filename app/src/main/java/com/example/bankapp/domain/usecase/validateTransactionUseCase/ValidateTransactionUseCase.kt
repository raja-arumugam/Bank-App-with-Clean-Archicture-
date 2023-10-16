package com.example.bankapp.domain.usecase.validateTransactionUseCase

import com.example.bankapp.common.AmountInputValidationType
import com.example.bankapp.domain.repository.BankRepository
import javax.inject.Inject

class ValidateTransactionUseCase @Inject constructor(private val repository: BankRepository) {
    operator fun invoke(
        amount: String,
        notes: String
    ): AmountInputValidationType {

        if (amount.isEmpty() && notes.isEmpty()) {
            return AmountInputValidationType.EmptyField
        }

        return AmountInputValidationType.Valid
    }
}