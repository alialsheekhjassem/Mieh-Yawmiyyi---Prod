package com.magma.miyyiyawmiyyi.android.model.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.magma.miyyiyawmiyyi.android.model.Prize
import java.io.Serializable

class PrizeConverter : Serializable {

    @TypeConverter
    fun fromObject(prize: Prize?): String? {
        if (prize == null) return null
        val gson = Gson()
        val type = object : TypeToken<Prize?>() {}.type
        return gson.toJson(prize, type)
    }

    @TypeConverter
    fun toObject(prize: String?): Prize? {
        if (prize == null) return null
        val gson = Gson()
        val type = object : TypeToken<Prize?>() {}.type
        return gson.fromJson(prize, type)
    }
}