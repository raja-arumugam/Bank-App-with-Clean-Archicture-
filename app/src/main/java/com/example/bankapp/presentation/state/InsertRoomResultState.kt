package com.example.bankapp.presentation.state

sealed class InsertRoomResultState<T>(
    val data: Int? = null,
    val message: String? = null
) {
    class Success<T>(data: Int) : InsertRoomResultState<T>(data)
    class Error<T>(message: String?, data: Int? = null) : InsertRoomResultState<T>(data, message)
    class Loading<T>(data: Int? = null) : InsertRoomResultState<T>(data)
}