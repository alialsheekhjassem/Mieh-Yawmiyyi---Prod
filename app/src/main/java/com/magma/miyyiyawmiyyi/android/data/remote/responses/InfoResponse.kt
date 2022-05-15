package com.magma.miyyiyawmiyyi.android.data.remote.responses

import com.google.gson.annotations.SerializedName
import com.magma.miyyiyawmiyyi.android.model.*

data class InfoResponse(

    @SerializedName("roundWinner")
    val roundWinner: WinnerObj?,
    @SerializedName("grandPrizeWinner")
    val grandPrizeWinner: WinnerObj?,
    @SerializedName("activeRound")
    val activeRound: Round?,
    @SerializedName("closedRound")
    val closedRound: Round?,
    @SerializedName("activeGrandPrize")
    val activeGrandPrize: GrandPrize?,
    @SerializedName("closedGrandPrize")
    val closedGrandPrize: GrandPrize?,
    @SerializedName("settings")
    var settings: List<Setting>? = arrayListOf(),
    @SerializedName("lastCompletedRoundUrl")
    val lastCompletedRoundUrl: String?,
    @SerializedName("userPoints")
    val userPoints: Int?,
    @SerializedName("ticketsAmountPerActiveRound")
    val ticketsAmountPerActiveRound: Int?,
    @SerializedName("ticketsAmountPerClosedRound")
    val ticketsAmountPerClosedRound: Int?,
)

