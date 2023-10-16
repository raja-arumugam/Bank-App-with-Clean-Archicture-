package com.example.bankapp.domain.usecase.getSelectedUser

import com.example.bankapp.common.Resources
import com.example.bankapp.data.local.entity.RegistrationModel
import com.example.bankapp.data.local.entity.UsersModel
import com.example.bankapp.domain.repository.BankRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetSelectedUserUseCase @Inject constructor(private val repository: BankRepository) {
    operator fun invoke(userID: Int): Flow<Resources<UsersModel>> =
        flow {
            try {
                emit(Resources.Loading())
                val users = repository.getSelectedUser(userID)
                emit(Resources.Success(users))
            } catch (e: Exception) {
                emit(Resources.Error(e.localizedMessage!!))
            }
        }
}