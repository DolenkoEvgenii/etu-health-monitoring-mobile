package ru.etu.monitoring.model.network.data.request

data class CreateTaskRequest(
    val orderId: String,
    val title: String,
    val dateTo: String,
    val quantity: Int
)