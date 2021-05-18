package ru.etu.monitoring.model.data


import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

data class Request(
    val age: String,
    @SerializedName("created_at")
    val createdAt: Date,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("middle_name")
    val middleName: String,
    @SerializedName("order_id")
    val orderId: String,
    var latitude: Float?,
    var longitude: Float?,
    val status: String,
    val symptoms: String,
    val temperature: String
) : Serializable {
    val name: String
        get() = "$lastName $firstName"

    val isNew: Boolean
        get() = status == "new"

    val isOpen: Boolean
        get() = status == "treatment"

    val isClosed: Boolean
        get() = status == "discharged"
}