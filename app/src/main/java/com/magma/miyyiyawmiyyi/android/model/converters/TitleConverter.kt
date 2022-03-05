package com.magma.miyyiyawmiyyi.android.model.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.magma.miyyiyawmiyyi.android.model.Title
import java.io.Serializable

class TitleConverter : Serializable {

    @TypeConverter
    fun fromObject(title: Title?): String? {
        if (title == null) return null
        val gson = Gson()
        val type = object : TypeToken<Title?>() {}.type
        return gson.toJson(title, type)
    }

    @TypeConverter
    fun toObject(title: String?): Title? {
        if (title == null) return null
        val gson = Gson()
        val type = object : TypeToken<Title?>() {}.type
        return gson.fromJson(title, type)
    }
}