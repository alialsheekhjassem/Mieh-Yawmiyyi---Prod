package com.magma.miyyiyawmiyyi.android.model.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.magma.miyyiyawmiyyi.android.model.Cancellation
import java.io.Serializable

class CancellationConverter : Serializable {

    @TypeConverter
    fun fromObject(cancellation: Cancellation?): String? {
        if (cancellation == null) return null
        val gson = Gson()
        val type = object : TypeToken<Cancellation?>() {}.type
        return gson.toJson(cancellation, type)
    }

    @TypeConverter
    fun toObject(cancellation: String?): Cancellation? {
        if (cancellation == null) return null
        val gson = Gson()
        val type = object : TypeToken<Cancellation?>() {}.type
        return gson.fromJson(cancellation, type)
    }
}