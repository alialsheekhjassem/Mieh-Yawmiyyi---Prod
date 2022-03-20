package com.magma.miyyiyawmiyyi.android.model

import com.google.gson.annotations.SerializedName

class GrandPrize(

    @SerializedName("_id")
    var _id: String?,

    @SerializedName("prize")
    var prize: Prize?,

    @SerializedName("status")
    var status: String?,

    @SerializedName("config")
    var config: GrandPrizeConfig?,

    @SerializedName("drawResultAt")
    var drawResultAt: String?,

    @SerializedName("cancellation")
    var cancellation: Cancellation?,

    @SerializedName("url")
    var url: String?,

    @SerializedName("createdAt")
    var createdAt: String?,

    @SerializedName("closedAt")
    var closedAt: String?,

    @SerializedName("modifiedAt")
    var modifiedAt: String?,

    @SerializedName("number")
    var number: Int?,

)
