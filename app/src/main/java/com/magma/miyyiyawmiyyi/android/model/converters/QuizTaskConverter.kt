package com.magma.miyyiyawmiyyi.android.model.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.magma.miyyiyawmiyyi.android.model.QuizTask
import java.io.Serializable

class QuizTaskConverter : Serializable {

    @TypeConverter
    fun fromObject(quiz: QuizTask?): String? {
        if (quiz == null) return null
        val gson = Gson()
        val type = object : TypeToken<QuizTask?>() {}.type
        return gson.toJson(quiz, type)
    }

    @TypeConverter
    fun toObject(quiz: String?): QuizTask? {
        if (quiz == null) return null
        val gson = Gson()
        val type = object : TypeToken<QuizTask?>() {}.type
        return gson.fromJson(quiz, type)
    }
}