package com.magma.miyyiyawmiyyi.android.model.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.magma.miyyiyawmiyyi.android.model.GiftCard
import java.io.Serializable

class GiftConverter : Serializable {

    @TypeConverter
    fun fromObject(giftCard: GiftCard?): String? {
        if (giftCard == null) return null
        val gson = Gson()
        val type = object : TypeToken<GiftCard?>() {}.type
        return gson.toJson(giftCard, type)
    }

    @TypeConverter
    fun toObject(giftCard: String?): GiftCard? {
        if (giftCard == null) return null
        val gson = Gson()
        val type = object : TypeToken<GiftCard?>() {}.type
        return gson.fromJson(giftCard, type)
    }
}