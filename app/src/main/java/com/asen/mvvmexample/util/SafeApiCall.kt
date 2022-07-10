package com.asen.mvvmexample.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend inline fun <T> safeApiCall(
    crossinline body: suspend () -> T
): Resource<T> {
    return try {
        // blocking block
        val response = withContext(Dispatchers.IO) {
            body()
        }
        Resource.Success(response)
    } catch (e: Exception) {
        Resource.Error(e)
    }
}