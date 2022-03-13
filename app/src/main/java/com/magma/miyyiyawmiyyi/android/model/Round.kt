package com.magma.miyyiyawmiyyi.android.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.magma.miyyiyawmiyyi.android.model.converters.*

@Entity(tableName = "round_card")
class Round(

    @PrimaryKey
    @ColumnInfo(name = "_id")
    var _id: String,

    @TypeConverters(PrizeConverter::class)
    @ColumnInfo(name = "prize")
    var prize: Prize?,

    @ColumnInfo(name = "type")
    var type: String?,

    @ColumnInfo(name = "status")
    var status: String?,

    @ColumnInfo(name = "url")
    var url: String?,

    @TypeConverters(ConfigConverter::class)
    @ColumnInfo(name = "config")
    var config: Config?,

    @TypeConverters(FixedStartDrawConverter::class)
    @ColumnInfo(name = "fixedStartDraw")
    var fixedStartDraw: FixedStartDraw?,

    @TypeConverters(FixedTicketsDrawConverter::class)
    @ColumnInfo(name = "fixedTicketsDraw")
    var fixedTicketsDraw: FixedTicketsDraw?,

    @TypeConverters(CancellationConverter::class)
    @ColumnInfo(name = "cancellation")
    var cancellation: Cancellation?,

    @ColumnInfo(name = "createdAt")
    var createdAt: String?,

    @ColumnInfo(name = "closedAt")
    var closedAt: String?,

    @ColumnInfo(name = "modifiedAt")
    var modifiedAt: String?,

    @ColumnInfo(name = "number")
    var number: Int?,

    )
