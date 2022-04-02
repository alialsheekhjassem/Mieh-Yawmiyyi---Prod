package com.magma.miyyiyawmiyyi.android.data.remote.responses

import com.google.gson.annotations.SerializedName

data class RoundStatisticsResponse(

    @SerializedName("maxTickets")
    val maxTickets: Int?,
    @SerializedName("availableTickets")
    val availableTickets: Int?,
    @SerializedName("issuedTickets")
    val issuedTickets: Int?,
)

