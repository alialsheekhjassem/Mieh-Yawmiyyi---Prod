package com.magma.miyyiyawmiyyi.android.data.remote.responses

import com.google.gson.annotations.SerializedName
import com.magma.miyyiyawmiyyi.android.model.Account
import com.magma.miyyiyawmiyyi.android.model.Info
import com.magma.miyyiyawmiyyi.android.model.Invite

data class MyAccountResponse(

    @SerializedName("account")
    var account: Account?,
    @SerializedName("sentInvites")
    val sentInvites: Int?,
    @SerializedName("fulfilledInvites")
    val fulfilledInvites: Int?,
)

