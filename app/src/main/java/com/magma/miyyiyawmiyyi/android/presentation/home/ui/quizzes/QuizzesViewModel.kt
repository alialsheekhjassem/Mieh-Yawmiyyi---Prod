package com.magma.miyyiyawmiyyi.android.presentation.home.ui.quizzes

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.magma.miyyiyawmiyyi.android.data.remote.controller.Resource
import com.magma.miyyiyawmiyyi.android.data.remote.requests.MarkAsDoneTasksRequest
import kotlinx.coroutines.CoroutineScope
import com.magma.miyyiyawmiyyi.android.data.repository.DataRepository
import com.magma.miyyiyawmiyyi.android.model.TaskObj
import com.magma.miyyiyawmiyyi.android.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class QuizzesViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    override val coroutineContext: CoroutineContext,
) : ViewModel(), CoroutineScope {

    val tasksDb = MutableLiveData<Event<List<TaskObj>>>()
    val actions = MutableLiveData<Event<QuizzesActions>>()

    fun loadAllTasks(type: String) {
        // save feed list into database
        launch {
            val list = withContext(Dispatchers.IO) {
                dataRepository.loadAllTasks(type)
            }
            Log.d("TAG", "loadAllTasks: $list")
            tasksDb.value = Event(list)
        }
    }

    /**
     * Live data of requests list response.
     * */
    internal var response = MutableLiveData<Event<Resource<Any?>>>()

    fun doServerMarkAsDone(request: MarkAsDoneTasksRequest) {
        launch {
            //val token = dataRepository.getApiToken()
            response.value = Event(Resource.Loading())
            val result: Resource<Any?> =
                dataRepository.doServerMarkAsDone(request)
            Log.d("TAG", "doServerMarkAsDone: $result")
            response.value = Event(result)
        }
    }

    fun onAnswer1() {
        actions.value = Event(QuizzesActions.ANSWER_1_CLICKED)
    }

    fun onAnswer2() {
        actions.value = Event(QuizzesActions.ANSWER_2_CLICKED)
    }

    fun onAnswer3() {
        actions.value = Event(QuizzesActions.ANSWER_3_CLICKED)
    }

    fun onBackToTasks() {
        actions.value = Event(QuizzesActions.BACK_TO_TASKS_CLICKED)
    }

    fun isEnableAds(): Boolean {
        return dataRepository.isEnableAds()
    }

}