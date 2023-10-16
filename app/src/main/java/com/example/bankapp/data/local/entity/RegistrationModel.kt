package com.example.bankapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "registration")
data class RegistrationModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "password")
    val password: String,
    @ColumnInfo(name = "confirm_password")
    val confirmPassword: String,
    @ColumnInfo(name = "account_number")
    val accountNumber: String,
    @ColumnInfo(name = "ifsc_code")
    val ifscCode: String,
    @ColumnInfo(name = "bank_name")
    val bankName: String,
    @ColumnInfo(name = "amount")
    val amount: String
)