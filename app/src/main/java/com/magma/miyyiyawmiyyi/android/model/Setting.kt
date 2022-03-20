package com.magma.miyyiyawmiyyi.android.model

import com.google.gson.annotations.SerializedName

class Setting (
    @SerializedName("_id")
    var _id: String?,

    @SerializedName("name")
    var name: String?,

    @SerializedName("description")
    var description: Title?,

    @SerializedName("type")
    var type: String?,

    @SerializedName("default")
    var default: String?,

    @SerializedName("value")
    var value: String?,

    @SerializedName("createdAt")
    var createdAt: String?,

    @SerializedName("modifiedAt")
    var modifiedAt: String?,
)