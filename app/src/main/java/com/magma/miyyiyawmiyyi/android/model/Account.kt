package com.magma.miyyiyawmiyyi.android.model

import com.google.gson.annotations.SerializedName

class Account(

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
