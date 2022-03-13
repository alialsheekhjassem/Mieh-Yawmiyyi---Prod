package com.magma.miyyiyawmiyyi.android.model.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.magma.miyyiyawmiyyi.android.model.FixedTicketsDraw
import java.io.Serializable

class FixedTicketsDrawConverter : Serializable {

    @TypeConverter
    fun fromObject(draw: FixedTicketsDraw?): String? {
        if (draw == null) return null
        val gson = Gson()
        val type = object : TypeToken<FixedTicketsDraw?>() {}.type
        return gson.toJson(draw, type)
    }

    @TypeConverter
    fun toObject(draw: String?): FixedTicketsDraw? {
        if (draw == null) return null
        val gson = Gson()
        val type = object : TypeToken<FixedTicketsDraw?>() {}.type
        return gson.fromJson(draw, type)
    }
}