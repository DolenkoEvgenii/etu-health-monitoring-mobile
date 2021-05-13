package ru.etu.monitoring.model.data

import java.text.SimpleDateFormat
import java.util.*

class User(
    val firstName: String,
    val lastName: String,
    val middleName: String,
    private val birthday: String,
    val isIll: Boolean,
    val role: String,
    val doctor: Doctor?
) {
    val birthdayStr: String
        get() {
            val oldFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val newFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

            return newFormat.format(oldFormat.parse(birthday) ?: Date())
        }
}