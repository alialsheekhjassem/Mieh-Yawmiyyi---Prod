package com.magma.miyyiyawmiyyi.android.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "country")
class Country(

    @PrimaryKey
    @ColumnInfo(name = "_id")
    var _id: String,

    @ColumnInfo(name = "name")
    var name: Title?,

    @ColumnInfo(name = "alpha2")
    var alpha2: String?,

    @ColumnInfo(name = "alpha3")
    var alpha3: String?,

    ) {
    override fun toString(): String {
        if (this.name == null) return ""
        val title = this.name
        return when (Locale.getDefault().language) {
            "en" -> title?.en ?: ""
            "ar" -> title?.ar ?: ""
            else -> ""
        }
    }
}
