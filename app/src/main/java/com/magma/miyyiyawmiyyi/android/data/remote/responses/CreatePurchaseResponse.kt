package com.magma.miyyiyawmiyyi.android.data.remote.responses

import com.google.gson.annotations.SerializedName

data class CreatePurchaseResponse(

    @SerializedName("user")
    val user: String?,
    @SerializedName("gift")
    val gift: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("code")
    val code: String?,
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("modifiedAt")
    val modifiedAt: String?,
    @SerializedName("_id")
    val _id: String?,
)

