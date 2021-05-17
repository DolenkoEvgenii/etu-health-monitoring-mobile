package ru.etu.monitoring.model.network.data.response.task

import ru.etu.monitoring.model.data.RequestTask

data class RequestTasksResponse(
    val active: List<RequestTask>,
    val done: List<RequestTask>,
    val removed: List<RequestTask>
)
