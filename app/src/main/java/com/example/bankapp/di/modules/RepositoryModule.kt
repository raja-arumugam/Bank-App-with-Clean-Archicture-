package com.example.bankapp.di.modules

import android.content.Context
import com.example.bankapp.data.local.db.BankDataBase
import com.example.bankapp.data.repository.BankRepositoryImpl
import com.example.bankapp.data.repository.DataStoreOperationImpl
import com.example.bankapp.domain.usecase.debitUseCase.DebitUseCase
import com.example.bankapp.domain.repository.BankRepository
import com.example.bankapp.domain.repository.DataStoreOperations
import com.example.bankapp.domain.usecase.clearDataStoreUseCase.ClearDataStoreUseCase
import com.example.bankapp.domain.usecase.loginUser.LoginUseCase
import com.example.bankapp.domain.usecase.UseCases
import com.example.bankapp.domain.usecase.addTranactionUseCase.AddTransactionUseCase
import com.example.bankapp.domain.usecase.depositUseCase.DepositUseCase
import com.example.bankapp.domain.usecase.getSelectedUser.GetSelectedUserUseCase
import com.example.bankapp.domain.usecase.getTransactionsUseCase.GetTransactionListUseCase
import com.example.bankapp.domain.usecase.getUserDetails.GetUserDetailUseCase
import com.example.bankapp.domain.usecase.getUsers.GetUsersListUseCase
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
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideUseCases(repository: BankRepository): UseCases {
        return UseCases(
            loginUseCase = LoginUseCase(repository),
            validateLoginUseCase = ValidateLoginUseCase(repository),
            validateRegisterUseCase = ValidateRegisterUseCase(repository),
            userAlreadyExistUseCase = UserAlreadyExistUseCase(repository),
            registerUseCase = RegistrationUseCase(repository),
            getUserDetailUseCase = GetUserDetailUseCase(repository),
            deleteUsersUseCase = DeleteUsersUseCase(repository),
            setUsersUseCase = SetUsersUseCase(repository),
            getUsersListUseCase = GetUsersListUseCase(repository),
            getSelectedUserUseCase = GetSelectedUserUseCase(repository),
            saveLoggedInUseCase = SaveLoggedInUseCase(repository),
            readLoggedInUseCase = ReadLoggedInUseCase(repository),
            saveLoggedInEmailUseCase = SaveLoggedInEmailUseCase(repository),
            readLoggedInEmailUseCase = ReadLoggedInEmailUseCase(repository),
            validateAmountUseCase = ValidateAmountUseCase(repository),
            depositUseCase = DepositUseCase(repository),
            addTransactionUseCase = AddTransactionUseCase(repository),
            validateTransactionUseCase = ValidateTransactionUseCase(repository),
            getTransactionListUseCase = GetTransactionListUseCase(repository),
            debitUseCase = DebitUseCase(repository),
            clearDataStoreUseCase = ClearDataStoreUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideLocalDataSource(database: BankDataBase, dataStore: DataStoreOperations): BankRepository {
        return BankRepositoryImpl(database,dataStore)
    }

    @Provides
    @Singleton
    fun provideDataStoreOperation(
        context: Context
    ): DataStoreOperations {
        return DataStoreOperationImpl(context = context)
    }

}