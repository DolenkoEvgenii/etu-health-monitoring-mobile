package ru.etu.monitoring.model.data


import com.google.gson.annotations.SerializedName

data class RequestTask(
    val id: String,
    @SerializedName("done_at")
    val doneAt: String?,
    @SerializedName("order_id")
    val orderId: String,
    @SerializedName("planned_at")
    val plannedAt: String,
    @SerializedName("removed_at")
    val removedAt: String?,
    val title: String
)