package com.magma.miyyiyawmiyyi.android.data.remote.responses

import com.google.gson.annotations.SerializedName
import com.magma.miyyiyawmiyyi.android.model.PurchaseCard

data class GiftStorePurchasesResponse(

    @SerializedName("total")
    val total: Int,
    @SerializedName("items")
    val items: ArrayList<PurchaseCard>
)

