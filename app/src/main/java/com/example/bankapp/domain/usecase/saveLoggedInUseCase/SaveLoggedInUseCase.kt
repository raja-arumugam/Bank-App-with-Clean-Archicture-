package com.example.bankapp.domain.usecase.saveLoggedInUseCase

import com.example.bankapp.domain.repository.BankRepository
import javax.inject.Inject

class SaveLoggedInUseCase @Inject constructor(
    private val repository: BankRepository
) {
    suspend operator fun invoke(isLogged: Boolean) {
        repository.storeLoggedIn(isLoggedIn = isLogged)
    }

}