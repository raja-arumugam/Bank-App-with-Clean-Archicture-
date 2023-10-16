package com.example.bankapp.domain.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreOperations {

    suspend fun saveUserLoginState(login: Boolean)

    fun readUserLoginState(): Flow<Boolean>

    suspend fun saveUserEmail(email: String)

    fun readUserEmail(): Flow<String>

    suspend fun clearAllDataStore()
}