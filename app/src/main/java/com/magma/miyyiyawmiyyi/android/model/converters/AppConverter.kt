package com.magma.miyyiyawmiyyi.android.model.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.magma.miyyiyawmiyyi.android.model.App
import java.io.Serializable

class AppConverter : Serializable {

    @TypeConverter
    fun fromObject(app: App?): String? {
        if (app == null) return null
        val gson = Gson()
        val type = object : TypeToken<App?>() {}.type
        return gson.toJson(app, type)
    }

    @TypeConverter
    fun toObject(app: String?): App? {
        if (app == null) return null
        val gson = Gson()
        val type = object : TypeToken<App?>() {}.type
        return gson.fromJson(app, type)
    }
}