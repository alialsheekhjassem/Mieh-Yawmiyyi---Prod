package com.magma.miyyiyawmiyyi.android.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ticket")
class Ticket(

    @PrimaryKey
    @ColumnInfo(name = "_id")
    var _id: String,

    @ColumnInfo(name = "user")
    var user: String?,

    @ColumnInfo(name = "round")
    var round: String?,

    @ColumnInfo(name = "grandPrize")
    var grandPrize: String?,

    @ColumnInfo(name = "createdAt")
    var createdAt: String?,

    @ColumnInfo(name = "number")
    var number: String?,

    )
