package ru.etu.monitoring.model.network.data.response.request

import ru.etu.monitoring.model.data.HomePoint

data class SetHomePointResponse(
    val orderId: String,
    val point: HomePoint
)