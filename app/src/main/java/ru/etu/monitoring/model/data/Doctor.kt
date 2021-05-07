package ru.etu.monitoring.model.data

class Doctor(
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val phone: String
) {
    val fullName: String
        get() {
            return "$lastName $firstName $middleName".trim().replace("  ", " ")
        }
}