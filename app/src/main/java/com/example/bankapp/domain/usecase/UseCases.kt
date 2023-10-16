package com.example.bankapp.domain.usecase

import com.example.bankapp.domain.usecase.debitUseCase.DebitUseCase
import com.example.bankapp.domain.usecase.clearDataStoreUseCase.ClearDataStoreUseCase
import com.example.bankapp.domain.usecase.addTranactionUseCase.AddTransactionUseCase
import com.example.bankapp.domain.usecase.depositUseCase.DepositUseCase
import com.example.bankapp.domain.usecase.getSelectedUser.GetSelectedUserUseCase
import com.example.bankapp.domain.usecase.getTransactionsUseCase.GetTransactionListUseCase
import com.example.bankapp.domain.usecase.getUserDetails.GetUserDetailUseCase
import com.example.bankapp.domain.usecase.getUsers.GetUsersListUseCase
import com.example.bankapp.domain.usecase.loginUser.LoginUseCase
import com.example.bankapp.domain.usecase.readLoggedInEmailUseCase.ReadLoggedInEmailUseCase
import com.example.bankapp.domain.usecase.readLoggedInUseCase.ReadLoggedInUseCase
import com.example.bankapp.domain.usecase.registerUser.RegistrationUseCase
import com.example.bankapp.domain.usecase.saveLoggedInEmailUseCase.SaveLoggedInEmailUseCase
import com.example.bankapp.domain.usecase.saveLoggedInUseCase.SaveLoggedInUseCase
import com.example.bankapp.domain.usecase.setUsers.DeleteUsersUseCase
import com.example.bankapp.domain.usecase.setUsers.SetUsersUseCase
import com.example.bankapp.domain.usecase.validateAmountUseCase.ValidateAmountUseCase
import com.example.bankapp.domain.usecase.validateLoginUser.ValidateLoginUseCase
import com.example.bankapp.domain.usecase.validateRegisterUser.UserAlreadyExistUseCase
import com.example.bankapp.domain.usecase.validateRegisterUser.ValidateRegisterUseCase
import com.example.bankapp.domain.usecase.validateTransactionUseCase.ValidateTransactionUseCase

data class UseCases(
    val loginUseCase: LoginUseCase,
    val validateLoginUseCase: ValidateLoginUseCase,
    val validateRegisterUseCase: ValidateRegisterUseCase,
    val userAlreadyExistUseCase: UserAlreadyExistUseCase,
    val registerUseCase: RegistrationUseCase,
    val getUserDetailUseCase: GetUserDetailUseCase,
    val deleteUsersUseCase: DeleteUsersUseCase,
    val setUsersUseCase: SetUsersUseCase,
    val getUsersListUseCase: GetUsersListUseCase,
    val getSelectedUserUseCase: GetSelectedUserUseCase,
    val saveLoggedInUseCase: SaveLoggedInUseCase,
    val readLoggedInUseCase: ReadLoggedInUseCase,
    val saveLoggedInEmailUseCase: SaveLoggedInEmailUseCase,
    val readLoggedInEmailUseCase: ReadLoggedInEmailUseCase,
    val validateAmountUseCase: ValidateAmountUseCase,
    val depositUseCase: DepositUseCase,
    val addTransactionUseCase: AddTransactionUseCase,
    val validateTransactionUseCase: ValidateTransactionUseCase,
    val getTransactionListUseCase: GetTransactionListUseCase,
    val debitUseCase: DebitUseCase,
    val clearDataStoreUseCase: ClearDataStoreUseCase
)