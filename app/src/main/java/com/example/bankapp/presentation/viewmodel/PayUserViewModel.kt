package com.example.bankapp.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankapp.R
import com.example.bankapp.common.AmountInputValidationType
import com.example.bankapp.common.Resources
import com.example.bankapp.data.local.entity.TransactionModel
import com.example.bankapp.domain.usecase.UseCases
import com.example.bankapp.presentation.state.AddTranactionsState
import com.example.bankapp.presentation.state.DebitState
import com.example.bankapp.presentation.state.DepositState
import com.example.bankapp.presentation.state.GetUserState
import com.example.bankapp.presentation.state.InsertRoomResultState
import com.example.bankapp.presentation.state.SelectedUserState
import com.example.bankapp.presentation.state.ValidateTransactionState
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

class PayUserViewModel @Inject constructor(
    private val useCases: UseCases,
    private val context: Context
) : ViewModel() {

    private val _selectedUser = MutableStateFlow(SelectedUserState())
    val selectedUser: StateFlow<SelectedUserState> = _selectedUser.asStateFlow()

    private val _getUser = MutableStateFlow(GetUserState())
    val getUser: StateFlow<GetUserState> = _getUser.asStateFlow()

    private var _validateTransactionState = MutableStateFlow(ValidateTransactionState())
    val validateTransactionState: StateFlow<ValidateTransactionState> =
        _validateTransactionState.asStateFlow()

    private val _addTransactionState = MutableStateFlow(AddTranactionsState())
    val addTransactionState: StateFlow<AddTranactionsState> = _addTransactionState.asStateFlow()

    private val _amountDebitState = MutableStateFlow(DebitState())
    val amountDebitState: StateFlow<DebitState> = _amountDebitState.asStateFlow()

    val _loggedInEmail = MutableStateFlow("")
    val loggedInEmail: StateFlow<String> = _loggedInEmail

    var sender_ID = MutableStateFlow(0)
    var sender_Amount = MutableStateFlow(0)

    var receiver_ID = MutableStateFlow(0)
    var receiver_Name = MutableStateFlow("")
    var receiver_Bank = MutableStateFlow("")
    var receiver_Image = MutableStateFlow("")
    var refNumber = MutableStateFlow(0)

    private var getUserJob: Job? = null
    private var getLoggedUserJob: Job? = null
    private var addTransactionJob: Job? = null

    // get logged-in user email
    suspend fun getLoggedInEmail() {
        viewModelScope.launch(Dispatchers.IO) {
            _loggedInEmail.value =
                useCases.readLoggedInEmailUseCase().stateIn(viewModelScope).value
        }
    }

    // get logged-in user data
    fun getLoggedUserData(email: String) {
        getLoggedUserJob?.cancel()

        getLoggedUserJob = useCases.getUserDetailUseCase(email).onEach { result ->
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

    // selected user data
    fun getUserData(userID: Int) {
        getUserJob?.cancel()

        getUserJob = useCases.getSelectedUserUseCase(userID).onEach { result ->

            when (result) {
                is Resources.Loading -> {
                    _selectedUser.update {
                        it.copy(isLoading = true)
                    }
                    _selectedUser.update {
                        it.copy(error = null)
                    }
                }

                is Resources.Error -> {
                    _selectedUser.update {
                        it.copy(isLoading = false)
                    }
                    _selectedUser.update {
                        it.copy(error = result.message)
                    }
                }

                is Resources.Success -> {
                    _selectedUser.update {
                        it.copy(isLoading = false)
                    }
                    _selectedUser.update {
                        it.copy(error = null)
                    }

                    _selectedUser.update {
                        it.copy(user = result.data)
                    }
                }

                else -> Unit
            }
        }.launchIn(viewModelScope)
    }

    // validation check
    fun checkInputValidation(amount: String, notes: String) {

        _validateTransactionState.update {
            it.copy(amountInput = amount)
        }
        _validateTransactionState.update {
            it.copy(notesInput = notes)
        }

        val validationResult =
            useCases.validateTransactionUseCase(amount, notes)

        processInputValidationType(validationResult)
    }

    private fun processInputValidationType(validationResult: AmountInputValidationType) {
        when (validationResult) {
            AmountInputValidationType.EmptyField -> {
                _validateTransactionState.update {
                    it.copy(
                        errorMessageInput = context.resources.getString(R.string.empty_fields_left),
                        isInputValid = false
                    )
                }
            }

            AmountInputValidationType.Valid -> {
                _validateTransactionState.update {
                    it.copy(errorMessageInput = null, isInputValid = true)
                }
            }
        }
    }

    // Pay Amount to Friends
    suspend fun payAmount() {
        addTransactionJob?.cancel()

        addTransactionJob = useCases.addTransactionUseCase(
            setTransaction()
        ).onEach { result ->

            when (result) {
                is InsertRoomResultState.Error -> {
                    _addTransactionState.update { it.copy(isLoading = false) }
                    _addTransactionState.update { it.copy(error = result.message) }
                }

                is InsertRoomResultState.Loading -> {
                    _addTransactionState.update { it.copy(isLoading = true) }
                }

                is InsertRoomResultState.Success -> {
                    _addTransactionState.update { it.copy(isLoading = false) }
                    _addTransactionState.update { it.copy(error = null) }
                    _addTransactionState.update { it.copy(addTransactions = result.data) }
                }

                else -> Unit
            }
        }.launchIn(viewModelScope)
    }

    // Debit amount from logged-in user after transaction success
    fun debitAmount() {
        viewModelScope.launch(Dispatchers.IO) {
            useCases.debitUseCase(
                sender_Amount,
                _validateTransactionState.value.amountInput.toInt(),
                loggedInEmail.value
            )
                .let { result ->
                    if (result > 0) {
                        _amountDebitState.update {
                            it.copy(deposit = true)
                        }
                    } else {
                        _amountDebitState.update {
                            it.copy(deposit = false)
                        }
                    }
                }
        }
    }

    private fun setTransaction(): List<TransactionModel> = listOf(
        TransactionModel(
            refNumber.value,
            senderId = sender_ID.value,
            receiverId = receiver_ID.value,
            receiverName = receiver_Name.value,
            receiverBank = receiver_Bank.value,
            receiverAvatar = receiver_Image.value,
            _validateTransactionState.value.amountInput,
            _validateTransactionState.value.notesInput,
        )
    )
}