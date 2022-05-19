package com.magma.miyyiyawmiyyi.android.data.remote.responses

import com.google.gson.annotations.SerializedName
import com.magma.miyyiyawmiyyi.android.model.*

data class InfoResponse(

    @SerializedName("roundWinner")
    val roundWinner: WinnerObj?,
    @SerializedName("grandPrizeWinner")
    val grandPrizeWinner: WinnerObj?,
    @SerializedName("currentRound")
    val currentRound: Round?,
    @SerializedName("currentGrandPrize")
    val currentGrandPrize: GrandPrize?,
    @SerializedName("settings")
    var settings: List<Setting>? = arrayListOf(),
    @SerializedName("lastCompletedRoundUrl")
    val lastCompletedRoundUrl: String?,
    @SerializedName("userPoints")
    val userPoints: Int?,
    @SerializedName("currentRoundTickets")
    var currentRoundTickets: Int?,
)

