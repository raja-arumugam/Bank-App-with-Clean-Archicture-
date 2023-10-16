package com.example.bankapp.domain.usecase.setUsers

import com.example.bankapp.data.local.entity.UsersModel
import com.example.bankapp.domain.repository.BankRepository
import com.example.bankapp.presentation.state.InsertRoomResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SetUsersUseCase @Inject constructor(
    private val repository: BankRepository
) {
    suspend operator fun invoke(usersList: List<UsersModel>): Flow<InsertRoomResultState<UsersModel>> =
        flow {
            try {
                emit(InsertRoomResultState.Loading())
                val state: List<Long> = repository.insertUsers(usersList)
                for (i in state.indices) {
                    emit(InsertRoomResultState.Success(i))
                }
            } catch (e: Exception) {
                emit(InsertRoomResultState.Error(e.localizedMessage))
            }
        }
}