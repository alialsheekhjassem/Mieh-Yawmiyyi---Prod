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

    fun getTasks(limit: Int, offset: Int) {
        launch {
            //val token = dataRepository.getApiToken()
            response.value = Event(Resource.Loading())
            val result: Resource<TasksResponse> =
                dataRepository.getTasks(limit, offset)
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

    fun deleteAndSaveTasks(response: TasksResponse) {
        // save feed list into database
        launch {
            withContext(Dispatchers.IO)
            {
                val items = dataRepository.loadAllTasks()
                deleteAndSaveTasks(response.items)
                Log.d("TAG", "deleteAndSaveTasks: $items")
            }
        }
    }

    fun deleteTask(taskId: String) {
        // save feed list into database
        launch {
            withContext(Dispatchers.IO)
            {
                val items = dataRepository.deleteTask(taskId)
                Log.d("TAG", "deleteAndSaveTasks: $items")
            }
        }
    }

    private fun deleteAndSaveTasks(tasksResponse: ArrayList<TaskObj>) {
        // save feed list into database
        launch {
            withContext(Dispatchers.IO)
            {
                val ids = dataRepository.deleteAllTasks()
                saveTasks(tasksResponse)
                Log.d("TAG", "deleteAndSaveTasks: $ids")
            }
        }
    }

    private fun saveTasks(tasksResponse: ArrayList<TaskObj>) {
        // save feed list into database
        launch {
            withContext(Dispatchers.IO)
            {
                val ids = dataRepository.insertTaskList(tasksResponse)
                loadAllTasks(Const.TYPE_SOCIAL_MEDIA)
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