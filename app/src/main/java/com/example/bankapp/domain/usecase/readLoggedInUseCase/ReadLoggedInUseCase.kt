package com.example.bankapp.domain.usecase.readLoggedInUseCase

import com.example.bankapp.domain.repository.BankRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReadLoggedInUseCase @Inject constructor(
    private val repository: BankRepository
) {
    suspend operator fun invoke(): Flow<Boolean> {
        return repository.readUserLoggedInState()
    }
}