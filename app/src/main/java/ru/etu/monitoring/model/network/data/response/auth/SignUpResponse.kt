package ru.etu.monitoring.model.network.data.response.auth

data class SignUpResponse(
    val authKey: String,
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val birthday: String,
    val role: String
) {
    val isPatient: Boolean
        get() = role == "patient"

    val isDoctor: Boolean
        get() = !isPatient
}