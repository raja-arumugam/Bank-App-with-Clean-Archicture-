package com.example.bankapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "reference_id")
    val referenceId: Int,
    @ColumnInfo(name = "sender_id")
    val senderId: Int,
    @ColumnInfo(name = "receiver_id")
    val receiverId: Int,
    @ColumnInfo(name = "receiver_name")
    val receiverName: String,
    @ColumnInfo(name = "receiverBank")
    val receiverBank: String,
    @ColumnInfo(name = "receiver_avatar")
    val receiverAvatar: String,
    @ColumnInfo(name = "amount")
    val amount: String,
    @ColumnInfo(name = "notes")
    val notes: String
)