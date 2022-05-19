package com.magma.miyyiyawmiyyi.android.presentation.home.ui.live_stream

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.magma.miyyiyawmiyyi.android.data.remote.controller.Resource
import com.magma.miyyiyawmiyyi.android.data.remote.responses.InfoResponse
import com.magma.miyyiyawmiyyi.android.data.remote.responses.RoundStatisticsResponse
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
    internal var responseStatistics = MutableLiveData<Event<Resource<RoundStatisticsResponse>>>()
    val actions = MutableLiveData<Event<LiveStreamActions>>()
    internal var responseCountDown = MutableLiveData<Long>()
    internal var responseGoldenCountDown = MutableLiveData<Long>()

    fun onTasks() {
        actions.value = Event(LiveStreamActions.TASKS_CLICKED)
    }

    fun onWatchLastDraw() {
        actions.value = Event(LiveStreamActions.SHOW_LAST_DRAW_CLICKED)
    }

    fun onStartCountDown(longDate: Long) {
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

    fun onStartGoldenCountDown(longDate: Long) {
        if (longDate != 0L) {
            Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate({
                    val elapsedTime: Long =
                        longDate - System.currentTimeMillis()
                    if (elapsedTime <= 0) {
                        responseGoldenCountDown.postValue(0L)
                    } else responseGoldenCountDown.postValue(
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

    fun getRoundStatistics(isActiveRound: Boolean) {
        launch {
            //val token = dataRepository.getApiToken()
            responseStatistics.value = Event(Resource.Loading())
            val result: Resource<RoundStatisticsResponse> =
                dataRepository.getRoundStatistics(isActiveRound)
            Log.d("TAG", "getRoundStatistics: $result")
            responseStatistics.value = Event(result)
        }
    }


    /**
     * Live data of requests list response.
     * */
    internal var infoResponse = MutableLiveData<Event<Resource<InfoResponse>>>()

    fun getInfo() {
        launch {
            infoResponse.value = Event(Resource.Loading())
            val result: Resource<InfoResponse> =
                dataRepository.getInfo()
            Log.d("TAG", "getInfo: $result")
            infoResponse.value = Event(result)
        }
    }
}