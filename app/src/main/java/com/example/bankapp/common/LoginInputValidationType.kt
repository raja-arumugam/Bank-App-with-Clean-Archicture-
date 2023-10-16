package com.example.bankapp.common

enum class LoginInputValidationType {
    EmptyField,
    InvalidEmail,
    PasswordUpperCaseMissing,
    PasswordNumberMissing,
    PasswordSpecialCharMissing,
    PasswordTooShort,
    Valid
}