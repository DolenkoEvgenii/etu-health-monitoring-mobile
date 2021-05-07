package ru.etu.monitoring.model.network.data.response

import com.google.gson.annotations.SerializedName

class BaseResponse<T>(
        @SerializedName("data") private val _data: T?,
        @SerializedName("errorMessage") private val _errorMessage: String?,
        val status: Boolean) {

    val data: T
        get() {
            return _data ?: throw NullPointerException()
        }

    val errorMessage: String
        get() = _errorMessage ?: ""
}

