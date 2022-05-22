package com.magma.miyyiyawmiyyi.android.data.remote.responses

import com.google.gson.annotations.SerializedName
import com.magma.miyyiyawmiyyi.android.model.AdTask
import com.magma.miyyiyawmiyyi.android.model.QuizTask
import com.magma.miyyiyawmiyyi.android.model.Task

data class GroupedTasksResponse(

    @SerializedName("socialMedias")
    val socialMedias: ArrayList<Task>,
    @SerializedName("quizzes")
    val quizzes: ArrayList<QuizTask>,
    @SerializedName("ads")
    val ads: ArrayList<AdTask>
)

