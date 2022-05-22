package com.magma.miyyiyawmiyyi.android.data.remote.requests

import com.google.gson.annotations.SerializedName

class AccountRequest {

    @SerializedName("name")
    var name:String? = ""

    @SerializedName("image")
    var image:String? = null

    @SerializedName("birthdate")
    var birthdate:String? = ""

    @SerializedName("gender")
    var gender:String? = null

    @SerializedName("country")
    var country:String? = null

    @SerializedName("locale")
    var locale:String? = ""

    @SerializedName("firebaseFCMToken")
    var firebaseFCMToken:String? = null

    @SerializedName("phone")
    var phone:String? = null

    @SerializedName("token")
    var token:String? = null

}