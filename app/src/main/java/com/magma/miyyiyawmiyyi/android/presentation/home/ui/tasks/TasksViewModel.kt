package com.magma.miyyiyawmiyyi.android.presentation.home.ui.tasks

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.magma.miyyiyawmiyyi.android.data.remote.controller.Resource
import com.magma.miyyiyawmiyyi.android.data.remote.requests.MarkAsDoneTasksRequest
import com.magma.miyyiyawmiyyi.android.data.remote.responses.TasksResponse
import kotlinx.coroutines.CoroutineScope
import com.magma.miyyiyawmiyyi.android.data.repository.DataRepository
import com.magma.miyyiyawmiyyi.android.model.TaskObj
import com.magma.miyyiyawmiyyi.android.utils.Const
import com.magma.miyyiyawmiyyi.android.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class TasksViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    override val coroutineContext: CoroutineContext,
) : ViewModel(), CoroutineScope {

    /**
     * Live data of requests list response.
     * */
    internal var response = MutableLiveData<Event<Resource<TasksResponse>>>()
    internal var responseGenerate = MutableLiveData<Event<Resource<Any?>>>()
    val tasksDb = MutableLiveData<Event<List<TaskObj>>>()
    val actions = MutableLiveData<Event<TasksActions>>()

    fun onQuizzesClicked() {
        actions.value = Event(TasksActions.QUIZZES_CLICKED)
    }

    fun getTasksCount(): Int {
        return dataRepository.getTasksCount()
    }

    fun setTasksCount(count: Int) {
        dataRepository.setTasksCount(count)
    }

    fun generateTasks() {
        launch {
            //val token = dataRepository.getApiToken()
            responseGenerate.value = Event(Resource.Loading())
            val result: Resource<Any?> =
                dataRepository.generateTasks()
            Log.d("TAG", "generateTasks: $result")
            responseGenerate.value = Event(result)
        }
    }

    fun getTasks(limit: Int, offset: Int, done: Boolean?, type: String?) {
        launch {
            //val token = dataRepository.getApiToken()
            response.value = Event(Resource.Loading())
            val result: Resource<TasksResponse> =
                dataRepository.getTasks(limit, offset, done, type)
            Log.d("TAG", "getTasks: $result")
            response.value = Event(result)
        }
    }

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

    fun deleteTask(taskId: String) {
        // save feed list into database
        launch {
            withContext(Dispatchers.IO)
            {
                val items = dataRepository.deleteTask(taskId)
                loadAllTasks(Const.TYPE_SOCIAL_MEDIA)
                loadAllTasks(Const.TYPE_AD)
                loadAllTasks(Const.TYPE_QUIZ)
                Log.d("TAG", "deleteAndSaveTasks: $items")
            }
        }
    }

    fun deleteAndSaveTasks(tasksResponse: ArrayList<TaskObj>, type: String?) {
        // save feed list into database
        launch {
            withContext(Dispatchers.IO)
            {
                if (!type.isNullOrEmpty())
                    dataRepository.deleteAllTasks(type)
                else dataRepository.deleteAllTasks()
                saveTasks(tasksResponse, type)
            }
        }
    }

    fun deleteAllTasks() {
        // save feed list into database
        launch {
            withContext(Dispatchers.IO)
            {
                dataRepository.deleteAllTasks()
                loadAllTasks(Const.TYPE_SOCIAL_MEDIA)
                loadAllTasks(Const.TYPE_AD)
                loadAllTasks(Const.TYPE_QUIZ)
            }
        }
    }

    private fun saveTasks(tasksResponse: ArrayList<TaskObj>, type: String?) {
        // save feed list into database
        launch {
            withContext(Dispatchers.IO)
            {
                val ids = dataRepository.insertTaskList(tasksResponse)
                type?.let { loadAllTasks(it) }
                Log.d("TAG", "saveTasks: $ids")
            }
        }
    }


    /**
     * Live data of requests list response.
     * */
    internal var responseMarkAsDone = MutableLiveData<Event<Resource<Any?>>>()

    fun doServerMarkAsDone(request: MarkAsDoneTasksRequest) {
        launch {
            //val token = dataRepository.getApiToken()
            responseMarkAsDone.value = Event(Resource.Loading())
            val result: Resource<Any?> =
                dataRepository.doServerMarkAsDone(request)
            Log.d("TAG", "doServerMarkAsDone: $result")
            responseMarkAsDone.value = Event(result)
        }
    }

}