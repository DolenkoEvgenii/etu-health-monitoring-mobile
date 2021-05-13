package ru.etu.monitoring.model.data


import com.google.gson.annotations.SerializedName

data class Request(
    val age: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("middle_name")
    val middleName: String,
    @SerializedName("order_id")
    val orderId: String,
    val symptoms: String,
    val temperature: String
)