package com.magma.miyyiyawmiyyi.android.data.remote.requests

import com.google.gson.annotations.SerializedName

class MarkAsDoneTasksRequest {

    @SerializedName("tasks")
    var tasks:ArrayList<String> = arrayListOf()

}