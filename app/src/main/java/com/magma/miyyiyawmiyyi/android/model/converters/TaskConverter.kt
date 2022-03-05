package com.magma.miyyiyawmiyyi.android.model.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.magma.miyyiyawmiyyi.android.model.Task
import java.io.Serializable

class TaskConverter : Serializable {

    @TypeConverter
    fun fromObject(task: Task?): String? {
        if (task == null) return null
        val gson = Gson()
        val type = object : TypeToken<Task?>() {}.type
        return gson.toJson(task, type)
    }

    @TypeConverter
    fun toObject(task: String?): Task? {
        if (task == null) return null
        val gson = Gson()
        val type = object : TypeToken<Task?>() {}.type
        return gson.fromJson(task, type)
    }
}