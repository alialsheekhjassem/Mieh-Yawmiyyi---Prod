package com.magma.miyyiyawmiyyi.android.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.magma.miyyiyawmiyyi.android.model.converters.TitleConverter

@Entity(tableName = "gift_card")
class GiftCard(

    @PrimaryKey
    @ColumnInfo(name = "_id")
    var _id: String,

    @TypeConverters(TitleConverter::class)
    @ColumnInfo(name = "name")
    var name: Title?,

    @TypeConverters(TitleConverter::class)
    @ColumnInfo(name = "cardName")
    var cardName: Title?,

    @ColumnInfo(name = "image")
    var image: String?,

    @ColumnInfo(name = "color")
    var color: String?,

    @ColumnInfo(name = "price")
    var price: Int?,

    @ColumnInfo(name = "status")
    var status: String?,

    @ColumnInfo(name = "createdAt")
    var createdAt: String?,

    @ColumnInfo(name = "modifiedAt")
    var modifiedAt: String?,

    )
