package com.magma.miyyiyawmiyyi.android.presentation.home.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.magma.miyyiyawmiyyi.android.data.remote.controller.Resource
import com.magma.miyyiyawmiyyi.android.data.remote.responses.InfoResponse
import kotlinx.coroutines.CoroutineScope
import com.magma.miyyiyawmiyyi.android.data.repository.DataRepository
import com.magma.miyyiyawmiyyi.android.model.Round
import com.magma.miyyiyawmiyyi.android.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class HomeViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    override val coroutineContext: CoroutineContext,
) : ViewModel(), CoroutineScope {

    val roundsDb = MutableLiveData<Event<List<Round>>>()

    fun loadAllRounds() {
        // save feed list into database
        launch {
            val list = withContext(Dispatchers.IO) {
                dataRepository.loadAllRounds()
            }
            Log.d("TAG", "loadAllTasks: $list")
            roundsDb.value = Event(list)
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