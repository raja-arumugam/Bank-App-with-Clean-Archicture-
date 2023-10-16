package com.example.bankapp.domain.usecase.validateRegisterUser

import android.util.Patterns
import com.example.bankapp.common.RegistrationInputValidationType
import com.example.bankapp.common.containsNumber
import com.example.bankapp.common.containsSpecialChar
import com.example.bankapp.common.containsUpperCase
import com.example.bankapp.common.isValidIFSC
import com.example.bankapp.domain.repository.BankRepository
import javax.inject.Inject

class ValidateRegisterUseCase @Inject constructor(private val repository: BankRepository) {

    operator fun invoke(
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
        bankName: String,
        ifscNumber: String,
        accountNumber: String,
        amount: String
    ): RegistrationInputValidationType {
        if (name.isEmpty() && email.isEmpty() && password.isEmpty() && confirmPassword.isEmpty()
            && bankName.isEmpty() && ifscNumber.isEmpty() && accountNumber.isEmpty() && amount.isEmpty()
        ) {
            return RegistrationInputValidationType.EmptyField
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return RegistrationInputValidationType.InvalidEmail
        }
        if (password != confirmPassword) {
            return RegistrationInputValidationType.PasswordsDoNotMatch
        }
        if (password.isNotEmpty() && password.count() < 8) {
            return RegistrationInputValidationType.PasswordTooShort
        }
        if (!password.containsNumber()) {
            return RegistrationInputValidationType.PasswordNumberMissing
        }
        if (!password.containsUpperCase()) {
            return RegistrationInputValidationType.PasswordUpperCaseMissing
        }
        if (!password.containsSpecialChar()) {
            return RegistrationInputValidationType.PasswordSpecialCharMissing
        }
        if (!ifscNumber.isValidIFSC(ifscNumber)) {
            return RegistrationInputValidationType.InvalidIFSC
        }
        if (amount < "100000") {
            return RegistrationInputValidationType.InSufficientAmount
        }

        return RegistrationInputValidationType.Valid
    }
}