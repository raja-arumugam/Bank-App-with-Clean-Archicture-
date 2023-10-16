package com.example.bankapp.data.repository

import com.example.bankapp.data.local.db.BankDataBase
import com.example.bankapp.data.local.entity.RegistrationModel
import com.example.bankapp.data.local.entity.TransactionModel
import com.example.bankapp.data.local.entity.UsersModel
import com.example.bankapp.domain.repository.BankRepository
import com.example.bankapp.domain.repository.DataStoreOperations
import kotlinx.coroutines.flow.Flow

class BankRepositoryImpl(
    private val bankDataBase: BankDataBase,
    private val dataStore: DataStoreOperations
) : BankRepository {

    override suspend fun storeLoggedIn(isLoggedIn: Boolean) {
        return dataStore.saveUserLoginState(isLoggedIn)
    }

    override suspend fun readUserLoggedInState(): Flow<Boolean> {
        return dataStore.readUserLoginState()
    }

    override suspend fun storeLoggedInEmail(loggedInEmail: String) {
        return dataStore.saveUserEmail(loggedInEmail)
    }

    override suspend fun readUserLoggedInEmail(): Flow<String> {
        return dataStore.readUserEmail()
    }

    override suspend fun clearDataStore() {
        return dataStore.clearAllDataStore()
    }

    override suspend fun registerUser(userReg: List<RegistrationModel>): List<Long> {
        return bankDataBase.authDao().insertRegistration(userReg)
    }

    override suspend fun checkUserExist(email: String): Int {
        return bankDataBase.authDao().userExist(email)
    }

    override suspend fun getUserLogin(email: String, password: String): Flow<RegistrationModel> {
        return bankDataBase.authDao().getLogin(email, password)
    }

    override suspend fun getUser(email: String): Flow<RegistrationModel> {
        return bankDataBase.authDao().getUser(email)
    }

    override suspend fun deleteUsers(): Int {
        return bankDataBase.authDao().deleteUsers()
    }

    override suspend fun insertUsers(users: List<UsersModel>): List<Long> {
        return bankDataBase.authDao().insertUsers(users)
    }

    override suspend fun getUsersList(): Flow<List<UsersModel>> {
        return bankDataBase.authDao().getUsersList()
    }

    override suspend fun getSelectedUser(userId: Int): Flow<UsersModel> {
        return bankDataBase.authDao().getSelectedUser(userId)
    }

    override suspend fun updateAmount(amount: String, email: String): Int {
        return bankDataBase.authDao().updateAmount(amount,email)
    }

    override suspend fun insertTransaction(transaction: List<TransactionModel>): List<Long> {
        return bankDataBase.authDao().insertTransaction(transaction)
    }

    override suspend fun getTransactionList(): Flow<List<TransactionModel>> {
        return bankDataBase.authDao().getTransactionList()
    }



}