package com.magma.miyyiyawmiyyi.android.data.remote.requests

import com.google.gson.annotations.SerializedName

class AccountRequest {

    @SerializedName("name")
    var name:String? = null

    @SerializedName("image")
    var image:String? = null

    @SerializedName("birthdate")
    var birthdate:String? = null

    @SerializedName("gender")
    var gender:String? = null

    @SerializedName("country")
    var country:String? = null

    @SerializedName("locale")
    var locale:String? = null
}