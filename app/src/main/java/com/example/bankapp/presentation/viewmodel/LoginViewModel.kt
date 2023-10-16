package com.example.bankapp.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankapp.R
import com.example.bankapp.common.LoginInputValidationType
import com.example.bankapp.common.Resources
import com.example.bankapp.domain.usecase.UseCases
import com.example.bankapp.presentation.state.LoginState
import com.example.bankapp.presentation.state.ValidateLoginState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val useCases: UseCases,
    private val context: Context
) : ViewModel() {

    private var _validateLoginState = MutableStateFlow(ValidateLoginState())
    val validateLoginState: StateFlow<ValidateLoginState> = _validateLoginState.asStateFlow()

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Empty)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    fun checkInputValidation(email: String, password: String) {

        _validateLoginState.update {
            it.copy(emailInput = email)
        }
        _validateLoginState.update {
            it.copy(passwordInput = password)
        }

        val validationResult =
            useCases.validateLoginUseCase(email, password)

        processInputValidationType(validationResult)
    }

    private fun processInputValidationType(validationResult: LoginInputValidationType) {
        when (validationResult) {
            LoginInputValidationType.EmptyField -> {
                _validateLoginState.update {
                    it.copy(
                        errorMessageInput = context.resources.getString(R.string.empty_fields_left),
                        isInputValid = false
                    )
                }
            }

            LoginInputValidationType.InvalidEmail -> {
                _validateLoginState.update {
                    it.copy(
                        errorMessageInput = context.resources.getString(R.string.invalid_email),
                        isInputValid = false
                    )
                }
            }

            LoginInputValidationType.PasswordTooShort -> {
                _validateLoginState.update {
                    it.copy(
                        errorMessageInput = context.resources.getString(R.string.password_too_short),
                        isInputValid = false
                    )
                }
            }

            LoginInputValidationType.PasswordUpperCaseMissing -> {
                _validateLoginState.update {
                    it.copy(
                        errorMessageInput = context.resources.getString(R.string.password_at_least_upper),
                        isInputValid = false
                    )
                }
            }

            LoginInputValidationType.PasswordSpecialCharMissing -> {
                _validateLoginState.update {
                    it.copy(
                        errorMessageInput = context.resources.getString(R.string.password_at_least_specialchar),
                        isInputValid = false
                    )
                }
            }

            LoginInputValidationType.PasswordNumberMissing -> {
                _validateLoginState.update {
                    it.copy(
                        errorMessageInput = context.resources.getString(R.string.password_at_least_number),
                        isInputValid = false
                    )
                }
            }

            LoginInputValidationType.Valid -> {
                _validateLoginState.update {
                    it.copy(errorMessageInput = null, isInputValid = true)
                }
            }
        }
    }

    suspend fun login() {
        _loginState.update {
            LoginState.Loading
        }

        useCases.loginUseCase(
            _validateLoginState.value.emailInput,
            _validateLoginState.value.passwordInput
        ).onEach { login ->
            _loginState.update {
                LoginState.Success(login)
            }
        }.catch { throwable: Throwable ->
            _loginState.update {
                LoginState.Error(throwable)
            }
        }.launchIn(viewModelScope)
    }

    suspend fun saveLoggedIn(isLoggedIn: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            useCases.saveLoggedInUseCase(isLogged = isLoggedIn)
        }
    }

    suspend fun saveLoggedInEmail(loggedInEmail: String) {
        viewModelScope.launch(Dispatchers.IO) {
            useCases.saveLoggedInEmailUseCase(email = loggedInEmail)
        }
    }

}