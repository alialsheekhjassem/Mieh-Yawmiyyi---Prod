package com.magma.miyyiyawmiyyi.android.model.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable

class AnyConverter : Serializable {

    @TypeConverter
    fun fromObject(any: Any?): String? {
        if (any == null) return null
        val gson = Gson()
        val type = object : TypeToken<Any?>() {}.type
        return gson.toJson(any, type)
    }

    @TypeConverter
    fun toObject(any: String?): Any? {
        if (any == null) return null
        val gson = Gson()
        val type = object : TypeToken<Any?>() {}.type
        return gson.fromJson(any, type)
    }
}