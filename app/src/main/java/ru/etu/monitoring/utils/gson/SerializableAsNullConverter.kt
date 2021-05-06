package ru.etu.monitoring.utils.gson

import com.google.gson.*
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class SerializableAsNullConverter : TypeAdapterFactory {

    override fun <T : Any?> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
        val declaredFields = type.rawType.declaredFields
        val nullableFieldNames = declaredFields.filter { it.declaredAnnotations.filterIsInstance<SerializeNull>().isNotEmpty() }.map {
            val annotation = it.getAnnotation(SerializedName::class.java)
            annotation?.value ?: it.name
        }
        if (nullableFieldNames.isEmpty()) return null
        val nonNullableFields = declaredFields.map {
            val annotation = it.getAnnotation(SerializedName::class.java)
            annotation?.value ?: it.name
        } - nullableFieldNames

        return object : TypeAdapter<T>() {
            private val delegateAdapter = gson.getDelegateAdapter(this@SerializableAsNullConverter, type)
            private val elementAdapter = gson.getAdapter(JsonElement::class.java)

            override fun write(writer: JsonWriter, value: T?) {
                val jsonObject = delegateAdapter.toJsonTree(value).asJsonObject

                nonNullableFields.forEach {
                    if (jsonObject.get(it) is JsonNull) {
                        jsonObject.remove(it)
                    }
                }

                writer.serializeNulls = true
                elementAdapter.write(writer, jsonObject)
            }

            override fun read(reader: JsonReader): T {
                return delegateAdapter.read(reader)
            }
        }
    }
}