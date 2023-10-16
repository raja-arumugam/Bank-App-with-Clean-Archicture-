package com.example.bankapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.bankapp.R

@Entity(tableName = "users")
data class UsersModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_Id")
    val userId: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "account_number")
    val accountNumber: String,
    @ColumnInfo(name = "ifsc_code")
    val ifscCode: String,
    @ColumnInfo(name = "bank_name")
    val bankName: String,
    @ColumnInfo(name = "amount")
    val amount: String,
    @ColumnInfo(name = "avatar")
    val avatar: String
)

fun setUsers(): List<UsersModel> = listOf(
    UsersModel(
        101,
        "Surya",
        "surya@gmail.com",
        "874737372",
        "HDFC0001821",
        "HDFC Bank",
        "120000",
        "https://reqres.in/img/faces/9-image.jpg"
    ),
    UsersModel(
        102,
        "Vedhika",
        "vedhika@gmail.com",
        "872973234",
        "IOB0001821",
        "IOB Bank",
        "30000",
        "https://reqres.in/img/faces/2-image.jpg"
    ),
    UsersModel(
        103,
        "Jothika",
        "jothika@gmail.com",
        "53074290",
        "ICICI0005828",
        "ICICI Bank",
        "50000",
        "https://reqres.in/img/faces/12-image.jpg"
    ),
)