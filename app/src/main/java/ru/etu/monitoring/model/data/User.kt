package ru.etu.monitoring.model.data

class User(
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val birthday: String,
    val isIll: Boolean,
    val doctor: Doctor?
)