package com.example.bankapp.domain.usecase.saveLoggedInEmailUseCase

import com.example.bankapp.domain.repository.BankRepository
import javax.inject.Inject

class SaveLoggedInEmailUseCase @Inject constructor(
    private val repository: BankRepository
) {
    suspend operator fun invoke(email: String) {
        repository.storeLoggedInEmail(loggedInEmail = email)
    }

}