package com.example.bankapp.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankapp.R
import com.example.bankapp.common.AmountInputValidationType
import com.example.bankapp.common.Resources
import com.example.bankapp.domain.usecase.UseCases
import com.example.bankapp.presentation.state.DepositState
import com.example.bankapp.presentation.state.GetUserState
import com.example.bankapp.presentation.state.ValidateAmountState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class DepositViewModel @Inject constructor(
    private val useCases: UseCases,
    private val context: Context
) : ViewModel() {

    private val _getUser = MutableStateFlow(GetUserState())
    val getUser: StateFlow<GetUserState> = _getUser.asStateFlow()

    val _loggedInEmail = MutableStateFlow("")
    val loggedInEmail: StateFlow<String> = _loggedInEmail

    private var _validateAmountState = MutableStateFlow(ValidateAmountState())
    val validateAmountState: StateFlow<ValidateAmountState> = _validateAmountState.asStateFlow()

    private val _amountDepositState = MutableStateFlow(DepositState())
    val amountDepositState: StateFlow<DepositState> = _amountDepositState.asStateFlow()

    var currentAmount = MutableStateFlow(0)

    private var getUserJob: Job? = null

    suspend fun getLoggedInEmail() {
        viewModelScope.launch(Dispatchers.IO) {
            _loggedInEmail.value =
                useCases.readLoggedInEmailUseCase().stateIn(viewModelScope).value
        }
    }

    fun getUserData(email: String) {
        getUserJob?.cancel()

        getUserJob = useCases.getUserDetailUseCase(email).onEach { result ->
            when (result) {
                is Resources.Loading -> {
                    _getUser.update {
                        it.copy(isLoading = true)
                    }
                    _getUser.update {
                        it.copy(error = null)
                    }
                }

                is Resources.Error -> {
                    _getUser.update {
                        it.copy(isLoading = false)
                    }
                    _getUser.update {
                        it.copy(error = result.message)
                    }
                }

                is Resources.Success -> {
                    _getUser.update {
                        it.copy(isLoading = false)
                    }
                    _getUser.update {
                        it.copy(error = null)
                    }

                    _getUser.update {
                        it.copy(userData = result.data)
                    }
                }

                else -> Unit
            }
        }.launchIn(viewModelScope)
    }

    fun checkInputValidation(amount: String) {

        _validateAmountState.update {
            it.copy(amountInput = amount)
        }

        val validationResult =
            useCases.validateAmountUseCase(amount)

        processInputValidationType(validationResult)
    }

    private fun processInputValidationType(validationResult: AmountInputValidationType) {
        when (validationResult) {
            AmountInputValidationType.EmptyField -> {
                _validateAmountState.update {
                    it.copy(
                        errorMessageInput = context.resources.getString(R.string.empty_fields_left),
                        isInputValid = false
                    )
                }
            }

            AmountInputValidationType.Valid -> {
                _validateAmountState.update {
                    it.copy(errorMessageInput = null, isInputValid = true)
                }
            }
        }
    }

    fun depositAmount() {
        viewModelScope.launch(Dispatchers.IO) {
            useCases.depositUseCase(
                currentAmount,
                _validateAmountState.value.amountInput.toInt(),
                loggedInEmail.value
            )
                .let { result ->
                    if (result > 0) {
                        _amountDepositState.update {
                            it.copy(deposit = true)
                        }
                    } else {
                        _amountDepositState.update {
                            it.copy(deposit = false)
                        }
                    }
                }
        }

    }

}