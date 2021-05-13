package ru.etu.monitoring.model.network.data.request

data class CreateIllnessRequest(
    val temperature: Float,
    val symptoms: String
)