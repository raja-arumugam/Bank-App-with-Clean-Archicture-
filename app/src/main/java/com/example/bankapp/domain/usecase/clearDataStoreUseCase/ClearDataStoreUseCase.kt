package com.example.bankapp.domain.usecase.clearDataStoreUseCase

import com.example.bankapp.domain.repository.BankRepository
import javax.inject.Inject

class ClearDataStoreUseCase @Inject constructor(
    private val repository: BankRepository
) {
    suspend operator fun invoke() {
        return repository.clearDataStore()
    }
}