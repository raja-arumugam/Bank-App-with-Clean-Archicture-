package com.example.bankapp.domain.usecase.getUsers

import com.example.bankapp.common.Resources
import com.example.bankapp.data.local.entity.UsersModel
import com.example.bankapp.domain.repository.BankRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUsersListUseCase @Inject constructor(private val repository: BankRepository) {
   suspend operator fun invoke(): Flow<Resources<List<UsersModel>>> =
        flow {
            try {
                emit(Resources.Loading())
                val users = repository.getUsersList()
                emit(Resources.Success(users))
            } catch (e: Exception) {
                emit(Resources.Error(e.localizedMessage!!))
            }
        }
}