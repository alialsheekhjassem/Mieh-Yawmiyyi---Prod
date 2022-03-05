package com.magma.miyyiyawmiyyi.android.model

import com.google.gson.annotations.SerializedName

class AccessToken(

    @SerializedName("token")
    var token: String?,

    @SerializedName("expiresIn")
    var expiresIn: Long?

)
