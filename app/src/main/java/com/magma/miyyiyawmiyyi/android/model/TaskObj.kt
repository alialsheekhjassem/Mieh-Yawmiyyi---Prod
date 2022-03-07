package com.magma.miyyiyawmiyyi.android.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.magma.miyyiyawmiyyi.android.model.converters.AnyConverter
import com.magma.miyyiyawmiyyi.android.model.converters.QuizTaskConverter
import com.magma.miyyiyawmiyyi.android.model.converters.TaskConverter

@Entity(tableName = "task_obj")
class TaskObj(

    @PrimaryKey
    @ColumnInfo(name = "_id")
    var _id: String,

    @ColumnInfo(name = "type")
    var type: String?,

    @ColumnInfo(name = "user")
    var user: String?,

    @TypeConverters(TaskConverter::class)
    @ColumnInfo(name = "smTask")
    var smTask: Task?,

    @TypeConverters(QuizTaskConverter::class)
    @ColumnInfo(name = "quizTask")
    var quizTask: QuizTask?,

    @TypeConverters(AnyConverter::class)
    @ColumnInfo(name = "adTask")
    var adTask: Any?,

    @TypeConverters(AnyConverter::class)
    @ColumnInfo(name = "round")
    var round: Any?,

    @ColumnInfo(name = "done")
    var done: Boolean?,

    @ColumnInfo(name = "ticket")
    var ticket: String?,

    @ColumnInfo(name = "createdAt")
    var createdAt: String?,

    )
