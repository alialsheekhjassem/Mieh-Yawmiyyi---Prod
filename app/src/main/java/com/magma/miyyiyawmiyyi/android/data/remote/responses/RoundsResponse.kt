package com.magma.miyyiyawmiyyi.android.data.remote.responses

import com.google.gson.annotations.SerializedName
import com.magma.miyyiyawmiyyi.android.model.Round

data class RoundsResponse(

    @SerializedName("total")
    val total: Int,
    @SerializedName("items")
    val items: ArrayList<Round>
)

