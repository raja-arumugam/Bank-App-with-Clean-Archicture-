package com.example.bankapp.domain.usecase.getUserDetails

import com.example.bankapp.common.Resources
import com.example.bankapp.data.local.entity.RegistrationModel
import com.example.bankapp.domain.repository.BankRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUserDetailUseCase @Inject constructor(private val repository: BankRepository) {
    operator fun invoke(email: String): Flow<Resources<RegistrationModel>> =
        flow {
            try {
                emit(Resources.Loading())
                val users = repository.getUser(email)
                emit(Resources.Success(users))
            } catch (e: Exception) {
                emit(Resources.Error(e.localizedMessage!!))
            }
        }
}