package ru.etu.monitoring.model.network.data.response.auth

data class ConfirmLoginResponse(
    val authKey: String,
    val role: String
) {
    val isPatient: Boolean
        get() = role == "patient"

    val isDoctor: Boolean
        get() = !isPatient
}