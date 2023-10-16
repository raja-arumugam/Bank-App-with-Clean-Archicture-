package com.example.bankapp.domain.usecase.validateRegisterUser

import com.example.bankapp.domain.repository.BankRepository
import javax.inject.Inject

class UserAlreadyExistUseCase @Inject constructor(private val repository: BankRepository) {

    suspend operator fun invoke(
        email: String
    ): Int {
        return repository.checkUserExist(email)
    }
}