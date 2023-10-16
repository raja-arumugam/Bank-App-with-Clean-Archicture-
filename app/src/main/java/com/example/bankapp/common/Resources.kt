package com.example.bankapp.common

import kotlinx.coroutines.flow.Flow

sealed class Resources<T>(
    val data: Flow<T>? = null,
    val message: String? = null
) {
    class Success<T>(data: Flow<T>) : Resources<T>(data)
    class Error<T>(message: String, data: Flow<T>? = null) : Resources<T>(data, message)
    class Loading<T>(data: Flow<T>? = null) : Resources<T>(data)
}