package ru.etu.monitoring.model.network.data.request

data class SignUpRequest(
    val phone: String,
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val birthDay: String,
    val code: String
)