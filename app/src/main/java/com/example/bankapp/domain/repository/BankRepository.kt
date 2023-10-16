package com.example.bankapp.domain.repository

import com.example.bankapp.data.local.entity.RegistrationModel
import com.example.bankapp.data.local.entity.TransactionModel
import com.example.bankapp.data.local.entity.UsersModel
import kotlinx.coroutines.flow.Flow

interface BankRepository {

    suspend fun storeLoggedIn(isLoggedIn: Boolean)

    suspend fun readUserLoggedInState(): Flow<Boolean>

    suspend fun storeLoggedInEmail(loggedInEmail: String)

    suspend fun readUserLoggedInEmail(): Flow<String>

    suspend fun clearDataStore()

    suspend fun registerUser(userReg: List<RegistrationModel>): List<Long>

    suspend fun checkUserExist(email: String): Int

    suspend fun getUserLogin(email: String, password: String): Flow<RegistrationModel>

    suspend fun getUser(email: String): Flow<RegistrationModel>

    suspend fun deleteUsers(): Int

    suspend fun insertUsers(users: List<UsersModel>): List<Long>

    suspend fun getUsersList(): Flow<List<UsersModel>>

    suspend fun getSelectedUser(userID: Int): Flow<UsersModel>

    suspend fun updateAmount(amount: String, email: String): Int

    suspend fun insertTransaction(transaction: List<TransactionModel>): List<Long>

    suspend fun getTransactionList(): Flow<List<TransactionModel>>


}