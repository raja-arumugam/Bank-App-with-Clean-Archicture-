package com.example.bankapp.domain.usecase.registerUser

import com.example.bankapp.data.local.entity.RegistrationModel
import com.example.bankapp.domain.repository.BankRepository
import com.example.bankapp.presentation.state.InsertRoomResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RegistrationUseCase @Inject constructor(
    private val repository: BankRepository
) {
    operator fun invoke(userReg: List<RegistrationModel>): Flow<InsertRoomResultState<RegistrationModel>> =

        flow {
            try {
                emit(InsertRoomResultState.Loading())
                val state: List<Long> = repository.registerUser(userReg)
                for (i in state.indices) {
                    emit(InsertRoomResultState.Success(i))
                }
            } catch (e: Exception) {
                emit(InsertRoomResultState.Error(e.localizedMessage))
            }
        }
}