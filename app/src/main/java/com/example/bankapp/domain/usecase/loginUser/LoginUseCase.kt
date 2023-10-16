package com.example.bankapp.domain.usecase.loginUser

import com.example.bankapp.common.Resources
import com.example.bankapp.data.local.entity.RegistrationModel
import com.example.bankapp.domain.repository.BankRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val repository: BankRepository) {
    suspend operator fun invoke(email: String, password: String):Flow<RegistrationModel> {
        return repository.getUserLogin(email, password)
    }
}