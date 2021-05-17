package ru.etu.monitoring.model.data


import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

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
) {
    val formattedDate: String
        get() {
            val realDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(plannedAt)
            return SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(realDate)
        }
}