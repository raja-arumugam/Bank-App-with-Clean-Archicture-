package com.example.bankapp.domain.usecase.validateAmountUseCase

import com.example.bankapp.common.AmountInputValidationType
import com.example.bankapp.domain.repository.BankRepository
import javax.inject.Inject

class ValidateAmountUseCase @Inject constructor(private val repository: BankRepository) {

    operator fun invoke(
        amount: String
    ): AmountInputValidationType {

        if (amount.isEmpty()) {
            return AmountInputValidationType.EmptyField
        }

        return AmountInputValidationType.Valid
    }
}