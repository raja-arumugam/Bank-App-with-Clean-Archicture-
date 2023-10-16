package com.example.bankapp.domain.usecase.readLoggedInEmailUseCase

import com.example.bankapp.domain.repository.BankRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReadLoggedInEmailUseCase @Inject constructor(
    private val repository: BankRepository
) {
    suspend operator fun invoke(): Flow<String> {
        return repository.readUserLoggedInEmail()
    }
}