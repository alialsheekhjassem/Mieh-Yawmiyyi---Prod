package com.magma.miyyiyawmiyyi.android.data.remote.requests

import com.google.gson.annotations.SerializedName

class LoginRequest {

    @SerializedName("phone")
    var phone:String? = null

    @SerializedName("token")
    var token:String? = null
}