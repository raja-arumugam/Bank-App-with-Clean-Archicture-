package com.example.bankapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bankapp.data.local.entity.RegistrationModel
import com.example.bankapp.data.local.entity.TransactionModel
import com.example.bankapp.data.local.entity.UsersModel
import kotlinx.coroutines.flow.Flow

@Dao
interface AuthDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRegistration(userReg: List<RegistrationModel>): List<Long>

    @Query("SELECT * FROM registration WHERE email =:email")
    fun userExist(email: String): Int

    @Query("SELECT * FROM registration WHERE email =:email AND password =:password")
    fun getLogin(email: String, password: String): Flow<RegistrationModel>

    @Query("SELECT * FROM registration WHERE email =:email")
    fun getUser(email: String): Flow<RegistrationModel>

    @Query("DELETE FROM users")
    suspend fun deleteUsers(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UsersModel>): List<Long>

    @Query("SELECT * FROM users ORDER BY user_Id ASC")
    fun getUsersList(): Flow<List<UsersModel>>

    @Query("SELECT * FROM users WHERE user_Id =:userID")
    fun getSelectedUser(userID: Int): Flow<UsersModel>

    @Query("UPDATE registration SET amount = :amount WHERE email = :email")
    fun updateAmount(amount: String,email: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(users: List<TransactionModel>): List<Long>

    @Query("SELECT * FROM transactions")
    fun getTransactionList(): Flow<List<TransactionModel>>
}