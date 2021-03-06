package ru.etu.monitoring.model.exception

class APIException(private val errorMessage: String,
                   val httpCode: Int? = null,
                   val body: String? = null) : Exception() {

    override fun getLocalizedMessage(): String {
        return errorMessage
    }
}