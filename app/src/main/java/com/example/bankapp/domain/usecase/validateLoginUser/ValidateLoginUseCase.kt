package com.example.bankapp.domain.usecase.validateLoginUser

import android.util.Patterns
import com.example.bankapp.common.LoginInputValidationType
import com.example.bankapp.common.containsNumber
import com.example.bankapp.common.containsSpecialChar
import com.example.bankapp.common.containsUpperCase
import com.example.bankapp.domain.repository.BankRepository
import javax.inject.Inject

class ValidateLoginUseCase @Inject constructor(private val repository: BankRepository) {

    operator fun invoke(
        email: String,
        password: String,
    ): LoginInputValidationType {
        if (email.isEmpty() && password.isEmpty()) {
            return LoginInputValidationType.EmptyField
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return LoginInputValidationType.InvalidEmail
        }
        if (password.isNotEmpty() && password.count() < 8) {
            return LoginInputValidationType.PasswordTooShort
        }
        if (!password.containsNumber()) {
            return LoginInputValidationType.PasswordNumberMissing
        }
        if (!password.containsUpperCase()) {
            return LoginInputValidationType.PasswordUpperCaseMissing
        }
        if (!password.containsSpecialChar()) {
            return LoginInputValidationType.PasswordSpecialCharMissing
        }

        return LoginInputValidationType.Valid
    }
}