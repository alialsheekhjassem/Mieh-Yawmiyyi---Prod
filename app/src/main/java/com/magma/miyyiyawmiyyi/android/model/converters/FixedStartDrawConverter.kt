package com.magma.miyyiyawmiyyi.android.model.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.magma.miyyiyawmiyyi.android.model.FixedStartDraw
import java.io.Serializable

class FixedStartDrawConverter : Serializable {

    @TypeConverter
    fun fromObject(draw: FixedStartDraw?): String? {
        if (draw == null) return null
        val gson = Gson()
        val type = object : TypeToken<FixedStartDraw?>() {}.type
        return gson.toJson(draw, type)
    }

    @TypeConverter
    fun toObject(draw: String?): FixedStartDraw? {
        if (draw == null) return null
        val gson = Gson()
        val type = object : TypeToken<FixedStartDraw?>() {}.type
        return gson.fromJson(draw, type)
    }
}