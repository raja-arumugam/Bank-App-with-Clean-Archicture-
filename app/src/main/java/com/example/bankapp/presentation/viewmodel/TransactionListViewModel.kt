package com.example.bankapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankapp.common.Resources
import com.example.bankapp.domain.usecase.UseCases
import com.example.bankapp.presentation.state.GetUserState
import com.example.bankapp.presentation.state.SelectedUserState
import com.example.bankapp.presentation.state.TransactionsListState
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

class TransactionListViewModel @Inject constructor(
    private val useCases: UseCases
) : ViewModel() {

    val _loggedInEmail = MutableStateFlow("")
    val loggedInEmail: StateFlow<String> = _loggedInEmail

    private val _getLoggedUser = MutableStateFlow(GetUserState())
    val getLoggedUser: StateFlow<GetUserState> = _getLoggedUser.asStateFlow()

    private val _transactionList = MutableStateFlow(TransactionsListState())
    val transactionList: StateFlow<TransactionsListState> = _transactionList.asStateFlow()

    private var getUserJob: Job? = null
    private var getTransactionListJob: Job? = null

    suspend fun getLoggedInEmail() {
        viewModelScope.launch(Dispatchers.IO) {
            _loggedInEmail.value =
                useCases.readLoggedInEmailUseCase().stateIn(viewModelScope).value
        }
    }

    fun getLoggedUserData(email: String) {
        getUserJob?.cancel()

        getUserJob = useCases.getUserDetailUseCase(email).onEach { result ->
            when (result) {
                is Resources.Loading -> {
                    _getLoggedUser.update {
                        it.copy(isLoading = true)
                    }
                    _getLoggedUser.update {
                        it.copy(error = null)
                    }
                }

                is Resources.Error -> {
                    _getLoggedUser.update {
                        it.copy(isLoading = false)
                    }
                    _getLoggedUser.update {
                        it.copy(error = result.message)
                    }
                }

                is Resources.Success -> {
                    _getLoggedUser.update {
                        it.copy(isLoading = false)
                    }
                    _getLoggedUser.update {
                        it.copy(error = null)
                    }

                    _getLoggedUser.update {
                        it.copy(userData = result.data)
                    }
                }
                else -> Unit
            }
        }.launchIn(viewModelScope)
    }

    suspend fun getAllTransactionsList() {
        getTransactionListJob?.cancel()

        getTransactionListJob = useCases.getTransactionListUseCase().onEach { result ->
            when (result) {
                is Resources.Loading -> {
                    _transactionList.update {
                        it.copy(isLoading = true)
                    }
                    _transactionList.update {
                        it.copy(error = null)
                    }
                }

                is Resources.Error -> {
                    _transactionList.update {
                        it.copy(isLoading = false)
                    }
                    _transactionList.update {
                        it.copy(error = result.message)
                    }
                }

                is Resources.Success -> {
                    _transactionList.update {
                        it.copy(isLoading = false)
                    }
                    _transactionList.update {
                        it.copy(error = null)
                    }

                    _transactionList.update {
                        it.copy(transaction = result.data)
                    }
                }
                else -> Unit
            }
        }.launchIn(viewModelScope)
    }


}