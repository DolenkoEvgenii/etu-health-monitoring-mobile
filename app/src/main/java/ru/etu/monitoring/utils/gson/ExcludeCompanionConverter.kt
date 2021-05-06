package ru.etu.monitoring.utils.gson

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class ExcludeCompanionConverter : TypeAdapterFactory {

    override fun <T : Any?> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
        val declaredFields = type.rawType.declaredFields

        return object : TypeAdapter<T>() {
            private val delegateAdapter = gson.getDelegateAdapter(this@ExcludeCompanionConverter, type)
            private val elementAdapter = gson.getAdapter(JsonElement::class.java)

            override fun write(writer: JsonWriter, value: T?) {
                val jsonElement = delegateAdapter.toJsonTree(value)
                if (jsonElement.isJsonObject) {
                    val jsonObject = jsonElement.asJsonObject
                    declaredFields.map { it.name }.forEach {
                        if (it == "companion") {
                            jsonObject.remove(it)
                        }
                    }

                    elementAdapter.write(writer, jsonObject)
                } else {
                    elementAdapter.write(writer, jsonElement)
                }
            }

            override fun read(reader: JsonReader): T {
                return delegateAdapter.read(reader)
            }
        }
    }
}