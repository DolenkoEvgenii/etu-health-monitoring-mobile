package ru.etu.monitoring.model.network.data.request

data class SetHomePointRequest(
    val orderId: String,
    val latitude: Double,
    val longitude: Double
)