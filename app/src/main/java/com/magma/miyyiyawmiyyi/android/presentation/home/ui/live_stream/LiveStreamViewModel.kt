package com.magma.miyyiyawmiyyi.android.presentation.home.ui.live_stream

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.magma.miyyiyawmiyyi.android.data.remote.controller.Resource
import com.magma.miyyiyawmiyyi.android.data.remote.responses.RoundsResponse
import kotlinx.coroutines.CoroutineScope
import com.magma.miyyiyawmiyyi.android.data.repository.DataRepository
import com.magma.miyyiyawmiyyi.android.utils.Event
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class LiveStreamViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    override val coroutineContext: CoroutineContext,
) : ViewModel(), CoroutineScope {

    /**
     * Live data of requests list response.
     * */
    internal var response = MutableLiveData<Event<Resource<RoundsResponse>>>()
    val actions = MutableLiveData<Event<LiveStreamActions>>()
    internal var responseCountDown = MutableLiveData<Long>()

    fun onTasks(){
        actions.value = Event(LiveStreamActions.TASKS_CLICKED)
    }

    fun onStartCountDown(longDate: Long){
        if (longDate != 0L) {
            Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate({
                    val elapsedTime: Long =
                        longDate - System.currentTimeMillis()
                    if (elapsedTime <= 0) {
                        responseCountDown.postValue(0L)
                    } else responseCountDown.postValue(
                        elapsedTime
                    )
                }, 0, 1, TimeUnit.SECONDS)
        }
    }

    fun getRounds(limit: Int, offset: Int, status: String?, id: String?) {
        launch {
            //val token = dataRepository.getApiToken()
            response.value = Event(Resource.Loading())
            val result: Resource<RoundsResponse> =
                dataRepository.getRounds(limit, offset, status, id)
            Log.d("TAG", "getRounds: $result")
            response.value = Event(result)
        }
    }
}