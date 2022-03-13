package com.magma.miyyiyawmiyyi.android.data.remote.responses

import com.google.gson.annotations.SerializedName
import com.magma.miyyiyawmiyyi.android.model.Info
import com.magma.miyyiyawmiyyi.android.model.Invite

data class MyAccountResponse(

    @SerializedName("_id")
    val _id: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("role")
    val role: String?,
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("modifiedAt")
    val modifiedAt: String?,
    @SerializedName("firebaseFCMToken")
    val firebaseFCMToken: String?,
    @SerializedName("info")
    val info: Info?,
    @SerializedName("points")
    val points: Int?,
    @SerializedName("locale")
    val locale: String?,
    @SerializedName("invite")
    val invite: Invite?,
)

