package com.example.bankapp.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankapp.R
import com.example.bankapp.common.RegistrationInputValidationType
import com.example.bankapp.data.local.entity.RegistrationModel
import com.example.bankapp.domain.usecase.UseCases
import com.example.bankapp.presentation.state.InsertRoomResultState
import com.example.bankapp.presentation.state.RegistrationState
import com.example.bankapp.presentation.state.UserExistState
import com.example.bankapp.presentation.state.ValidateRegisterState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegistrationViewModel @Inject constructor(
    private val useCases: UseCases,
    private val context: Context
) : ViewModel() {

    private var _validateRegisterState = MutableStateFlow(ValidateRegisterState())
    val validateRegisterState: StateFlow<ValidateRegisterState> =
        _validateRegisterState.asStateFlow()

    private val _registrationState = MutableStateFlow(RegistrationState())
    val registrationState: StateFlow<RegistrationState> = _registrationState.asStateFlow()

    private val _userExist = MutableStateFlow(UserExistState())
    val userExist: StateFlow<UserExistState> = _userExist.asStateFlow()

    private var job: Job? = null

    fun checkInputValidation(
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
        bankName: String,
        ifscNumber: String,
        accountNumber: String,
        amount: String
    ) {
        _validateRegisterState.update {
            it.copy(nameInput = name)
        }
        _validateRegisterState.update {
            it.copy(emailInput = email)
        }
        _validateRegisterState.update {
            it.copy(passwordInput = password)
        }
        _validateRegisterState.update {
            it.copy(confirmPasswordInput = confirmPassword)
        }
        _validateRegisterState.update {
            it.copy(bankNameInput = bankName)
        }
        _validateRegisterState.update {
            it.copy(ifscCodeInput = ifscNumber)
        }
        _validateRegisterState.update {
            it.copy(accountNumberInput = accountNumber)
        }
        _validateRegisterState.update {
            it.copy(amountInput = amount)
        }

        val validationResult =
            useCases.validateRegisterUseCase(
                name,
                email,
                password,
                confirmPassword,
                bankName,
                ifscNumber,
                accountNumber,
                amount
            )

        processInputValidationType(validationResult)
    }

    private fun processInputValidationType(type: RegistrationInputValidationType) {
        when (type) {
            RegistrationInputValidationType.EmptyField -> {
                _validateRegisterState.update {
                    it.copy(
                        errorMessageInput = context.resources.getString(R.string.empty_fields_left),
                        isInputValid = false
                    )
                }
            }

            RegistrationInputValidationType.InvalidEmail -> {
                _validateRegisterState.update {
                    it.copy(
                        errorMessageInput = context.resources.getString(R.string.invalid_email),
                        isInputValid = false
                    )
                }
            }

            RegistrationInputValidationType.PasswordTooShort -> {
                _validateRegisterState.update {
                    it.copy(
                        errorMessageInput = context.resources.getString(R.string.password_too_short),
                        isInputValid = false
                    )
                }
            }

            RegistrationInputValidationType.PasswordsDoNotMatch -> {
                _validateRegisterState.update {
                    it.copy(
                        errorMessageInput = context.resources.getString(R.string.passwords_do_not_match),
                        isInputValid = false
                    )
                }
            }

            RegistrationInputValidationType.PasswordUpperCaseMissing -> {
                _validateRegisterState.update {
                    it.copy(
                        errorMessageInput = context.resources.getString(R.string.password_at_least_upper),
                        isInputValid = false
                    )
                }
            }

            RegistrationInputValidationType.PasswordSpecialCharMissing -> {
                _validateRegisterState.update {
                    it.copy(
                        errorMessageInput = context.resources.getString(R.string.password_at_least_specialchar),
                        isInputValid = false
                    )
                }
            }

            RegistrationInputValidationType.PasswordNumberMissing -> {
                _validateRegisterState.update {
                    it.copy(
                        errorMessageInput = context.resources.getString(R.string.password_at_least_number),
                        isInputValid = false
                    )
                }
            }

            RegistrationInputValidationType.InvalidIFSC -> {
                _validateRegisterState.update {
                    it.copy(
                        errorMessageInput = context.resources.getString(R.string.invalid_ifsc),
                        isInputValid = false
                    )
                }
            }

            RegistrationInputValidationType.InSufficientAmount -> {
                _validateRegisterState.update {
                    it.copy(
                        errorMessageInput = context.resources.getString(R.string.insufficient_amount),
                        isInputValid = false
                    )
                }
            }

            RegistrationInputValidationType.Valid -> {
                _validateRegisterState.update {
                    it.copy(errorMessageInput = null, isInputValid = true)
                }
            }
        }
    }

    fun checkUserAlreadyExist() {
        viewModelScope.launch(Dispatchers.IO) {

            useCases.userAlreadyExistUseCase(
                _validateRegisterState.value.emailInput
            ).let { result ->
                if (result > 0) {
                    _userExist.update {
                        it.copy(exist = true, isLoading = null)
                    }
                } else {
                    _userExist.update {
                        it.copy(exist = false, isLoading = null)
                    }
                }
            }
        }
    }

    fun userRegistration() {
        viewModelScope.launch(Dispatchers.IO) {
            job?.cancel()

            val list: List<RegistrationModel> = listOf(
                RegistrationModel(
                    0,
                    _validateRegisterState.value.nameInput,
                    _validateRegisterState.value.emailInput,
                    _validateRegisterState.value.passwordInput,
                    _validateRegisterState.value.confirmPasswordInput,
                    _validateRegisterState.value.accountNumberInput,
                    _validateRegisterState.value.ifscCodeInput,
                    _validateRegisterState.value.bankNameInput,
                    _validateRegisterState.value.amountInput

                )
            )

            job = useCases.registerUseCase(list).onEach { result ->

                when (result) {
                    is InsertRoomResultState.Error -> {
                        _registrationState.update { it.copy(isLoading = false) }
                        _registrationState.update { it.copy(error = result.message) }
                    }

                    is InsertRoomResultState.Loading -> {
                        _registrationState.update { it.copy(isLoading = true) }
                    }

                    is InsertRoomResultState.Success -> {
                        _registrationState.update { it.copy(isLoading = false) }
                        _registrationState.update { it.copy(error = null) }
                        _registrationState.update { it.copy(userRegistration = result.data) }
                    }

                    else -> Unit
                }
            }.launchIn(viewModelScope)

        }
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