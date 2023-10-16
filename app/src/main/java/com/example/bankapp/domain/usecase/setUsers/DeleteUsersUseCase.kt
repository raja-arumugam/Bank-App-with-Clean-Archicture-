package com.example.bankapp.domain.usecase.setUsers

import com.example.bankapp.domain.repository.BankRepository
import javax.inject.Inject

class DeleteUsersUseCase @Inject constructor(
    private val repository: BankRepository
) {
    suspend operator fun invoke(): Int {
        return repository.deleteUsers()
    }

}