package com.example.bankapp.common

enum class RegistrationInputValidationType {
    EmptyField,
    InvalidEmail,
    PasswordsDoNotMatch,
    PasswordUpperCaseMissing,
    PasswordNumberMissing,
    PasswordSpecialCharMissing,
    PasswordTooShort,
    InvalidIFSC,
    InSufficientAmount,
    Valid
}