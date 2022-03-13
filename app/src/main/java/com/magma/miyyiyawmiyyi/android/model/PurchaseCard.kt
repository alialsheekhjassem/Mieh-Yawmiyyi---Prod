package com.magma.miyyiyawmiyyi.android.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.magma.miyyiyawmiyyi.android.model.converters.GiftConverter
import com.magma.miyyiyawmiyyi.android.model.converters.NameConverter

@Entity(tableName = "purchase_card")
class PurchaseCard(

    @PrimaryKey
    @ColumnInfo(name = "_id")
    var _id: String,

    @TypeConverters(NameConverter::class)
    @ColumnInfo(name = "user")
    var user: Name?,

    @TypeConverters(GiftConverter::class)
    @ColumnInfo(name = "gift")
    var gift: GiftCard?,

    @ColumnInfo(name = "status")
    var status: String?,

    @ColumnInfo(name = "code")
    var code: String?,

    @ColumnInfo(name = "createdAt")
    var createdAt: Int?,

    @ColumnInfo(name = "modifiedAt")
    var modifiedAt: String?,

    )
