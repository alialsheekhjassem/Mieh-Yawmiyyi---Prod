package com.magma.miyyiyawmiyyi.android.model.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.magma.miyyiyawmiyyi.android.model.Name
import java.io.Serializable

class NameConverter : Serializable {

    @TypeConverter
    fun fromObject(name: Name?): String? {
        if (name == null) return null
        val gson = Gson()
        val type = object : TypeToken<Name?>() {}.type
        return gson.toJson(name, type)
    }

    @TypeConverter
    fun toObject(name: String?): Name? {
        if (name == null) return null
        val gson = Gson()
        val type = object : TypeToken<Name?>() {}.type
        return gson.fromJson(name, type)
    }
}