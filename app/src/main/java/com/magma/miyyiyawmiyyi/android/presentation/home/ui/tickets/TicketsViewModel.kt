package com.magma.miyyiyawmiyyi.android.presentation.home.ui.tickets

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.magma.miyyiyawmiyyi.android.data.remote.controller.Resource
import com.magma.miyyiyawmiyyi.android.data.remote.responses.TicketsResponse
import kotlinx.coroutines.CoroutineScope
import com.magma.miyyiyawmiyyi.android.data.repository.DataRepository
import com.magma.miyyiyawmiyyi.android.model.Ticket
import com.magma.miyyiyawmiyyi.android.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class TicketsViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    override val coroutineContext: CoroutineContext,
) : ViewModel(), CoroutineScope {

    /**
     * Live data of requests list response.
     * */
    internal var response = MutableLiveData<Event<Resource<TicketsResponse>>>()
    val ticketsDb = MutableLiveData<Event<List<Ticket>>>()

    fun getTickets(limit: Int, offset: Int, round: String?, populate: Boolean?) {
        launch {
            //val token = dataRepository.getApiToken()
            response.value = Event(Resource.Loading())
            val result: Resource<TicketsResponse> =
                dataRepository.getTickets(limit, offset, round, populate)
            Log.d("TAG", "getTickets: $result")
            response.value = Event(result)
        }
    }

    fun loadAllTickets() {
        // save feed list into database
        launch {
            val list = withContext(Dispatchers.IO) {
                dataRepository.loadAllTickets()
            }
            Log.d("TAG", "loadAllTasks: $list")
            ticketsDb.value = Event(list)
        }
    }

    fun deleteAndSaveTickets(ticketsResponse: ArrayList<Ticket>) {
        // save feed list into database
        launch {
            withContext(Dispatchers.IO)
            {
                val ids = dataRepository.deleteAllTickets()
                saveTickets(ticketsResponse)
                Log.d("TAG", "deleteAndSaveTasks: $ids")
            }
        }
    }

    private fun saveTickets(ticketsResponse: ArrayList<Ticket>) {
        // save feed list into database
        launch {
            withContext(Dispatchers.IO)
            {
                val ids = dataRepository.insertTicketList(ticketsResponse)
                loadAllTickets()
                Log.d("TAG", "saveTickets: $ids")
            }
        }
    }
}