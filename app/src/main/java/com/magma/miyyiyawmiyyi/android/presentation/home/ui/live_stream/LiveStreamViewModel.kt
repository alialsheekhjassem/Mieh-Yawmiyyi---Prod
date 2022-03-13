package com.magma.miyyiyawmiyyi.android.presentation.home.ui.live_stream

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.magma.miyyiyawmiyyi.android.data.remote.controller.Resource
import com.magma.miyyiyawmiyyi.android.data.remote.responses.RoundsResponse
import kotlinx.coroutines.CoroutineScope
import com.magma.miyyiyawmiyyi.android.data.repository.DataRepository
import com.magma.miyyiyawmiyyi.android.utils.Const
import com.magma.miyyiyawmiyyi.android.utils.Event
import kotlinx.coroutines.launch
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