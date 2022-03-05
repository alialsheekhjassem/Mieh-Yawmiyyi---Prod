package com.magma.miyyiyawmiyyi.android.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.magma.miyyiyawmiyyi.android.data.local.repository.dao.TaskDao
import com.magma.miyyiyawmiyyi.android.model.TaskObj
import com.magma.miyyiyawmiyyi.android.model.converters.AnyConverter
import com.magma.miyyiyawmiyyi.android.model.converters.AppConverter
import com.magma.miyyiyawmiyyi.android.model.converters.TaskConverter
import com.magma.miyyiyawmiyyi.android.model.converters.TitleConverter

@Database(
    entities = [TaskObj::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    TitleConverter::class, TaskConverter::class,
    AnyConverter::class, AppConverter::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}