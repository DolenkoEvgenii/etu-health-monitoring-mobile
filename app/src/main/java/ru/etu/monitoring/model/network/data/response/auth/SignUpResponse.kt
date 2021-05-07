package ru.etu.monitoring.model.network.data.response.auth

data class SignUpResponse(
    val authKey: String,
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val birthday: String,
)