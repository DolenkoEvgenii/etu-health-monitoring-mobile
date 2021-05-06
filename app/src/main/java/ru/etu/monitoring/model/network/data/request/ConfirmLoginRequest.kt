package ru.etu.monitoring.model.network.data.request

data class ConfirmLoginRequest(
    val phone: String,
    val code: String
)