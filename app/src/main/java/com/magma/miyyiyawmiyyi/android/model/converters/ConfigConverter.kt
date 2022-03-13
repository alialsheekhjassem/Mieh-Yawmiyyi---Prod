package com.magma.miyyiyawmiyyi.android.model.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.magma.miyyiyawmiyyi.android.model.Config
import java.io.Serializable

class ConfigConverter : Serializable {

    @TypeConverter
    fun fromObject(config: Config?): String? {
        if (config == null) return null
        val gson = Gson()
        val type = object : TypeToken<Config?>() {}.type
        return gson.toJson(config, type)
    }

    @TypeConverter
    fun toObject(config: String?): Config? {
        if (config == null) return null
        val gson = Gson()
        val type = object : TypeToken<Config?>() {}.type
        return gson.fromJson(config, type)
    }
}