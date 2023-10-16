package com.example.bankapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankapp.domain.usecase.UseCases
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.stateIn

class SplashViewModel @Inject constructor(
    private val useCases: UseCases) : ViewModel() {

    private val _userLoggedIn = MutableStateFlow(false)
    val userLoggedIn: StateFlow<Boolean> = _userLoggedIn

    private val _loggedInEmail = MutableStateFlow("")
    val loggedInEmail: StateFlow<String> = _loggedInEmail

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _userLoggedIn.value =
                useCases.readLoggedInUseCase().stateIn(viewModelScope).value
        }

        viewModelScope.launch(Dispatchers.IO) {
            _loggedInEmail.value =
                useCases.readLoggedInEmailUseCase().stateIn(viewModelScope).value
        }

    }
}