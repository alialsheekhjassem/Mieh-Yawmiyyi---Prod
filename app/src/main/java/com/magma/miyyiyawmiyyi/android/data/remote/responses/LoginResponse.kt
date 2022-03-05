package com.magma.miyyiyawmiyyi.android.data.remote.responses

import com.google.gson.annotations.SerializedName
import com.magma.miyyiyawmiyyi.android.model.AccessToken


data class LoginResponse(

    @SerializedName("_id")
    val _id: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("role")
    val role: String?,
    @SerializedName("accessToken")
    val accessToken: AccessToken?,
    @SerializedName("refreshToken")
    val refreshToken: AccessToken?
)

