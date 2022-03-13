package com.magma.miyyiyawmiyyi.android.data.remote.responses

import com.google.gson.annotations.SerializedName
import com.magma.miyyiyawmiyyi.android.model.GiftCard

data class GiftStoreResponse(

    @SerializedName("total")
    val total: Int,
    @SerializedName("items")
    val items: ArrayList<GiftCard>
)

