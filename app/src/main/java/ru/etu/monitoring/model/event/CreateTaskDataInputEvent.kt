package ru.etu.monitoring.model.event

data class CreateTaskDataInputEvent(
    val title: String,
    val dateTo: String,
    val perDay: Int
)
