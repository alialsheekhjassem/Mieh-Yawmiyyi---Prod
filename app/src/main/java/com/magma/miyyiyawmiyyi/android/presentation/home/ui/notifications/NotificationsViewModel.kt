package com.magma.miyyiyawmiyyi.android.presentation.home.ui.notifications

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.magma.miyyiyawmiyyi.android.data.remote.controller.Resource
import com.magma.miyyiyawmiyyi.android.data.remote.responses.NotificationsResponse
import kotlinx.coroutines.CoroutineScope
import com.magma.miyyiyawmiyyi.android.data.repository.DataRepository
import com.magma.miyyiyawmiyyi.android.utils.Event
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class NotificationsViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    override val coroutineContext: CoroutineContext,
) : ViewModel(), CoroutineScope {

    internal var responseNotifications = MutableLiveData<Event<Resource<NotificationsResponse>>>()
    //val purchasesCardsDb = MutableLiveData<Event<List<Notification>>>()

    fun getNotifications(limit: Int, offset: Int) {
        launch {
            //val token = dataRepository.getApiToken()
            responseNotifications.value = Event(Resource.Loading())
            val result: Resource<NotificationsResponse> =
                dataRepository.getNotifications(limit, offset)
            Log.d("TAG", "getNotifications: $result")
            responseNotifications.value = Event(result)
        }
    }

    /*fun loadAllPurchaseCards() {
        // save feed list into database
        launch {
            val list = withContext(Dispatchers.IO) {
                dataRepository.loadAllPurchaseCards()
            }
            Log.d("TAG", "loadAllPurchaseCards: $list")
            purchasesCardsDb.value = Event(list)
        }
    }

    fun deleteAndSavePurchaseCards(response: GiftStorePurchasesResponse) {
        // save feed list into database
        launch {
            withContext(Dispatchers.IO)
            {
                val items = dataRepository.loadAllPurchaseCards()
                deleteAndSavePurchaseCards(response.items)
                Log.d("TAG", "deleteAndSavePurchaseCards: $items")
            }
        }
    }

    private fun deleteAndSavePurchaseCards(purchasesResponse: ArrayList<PurchaseCard>) {
        // save feed list into database
        launch {
            withContext(Dispatchers.IO)
            {
                val ids = dataRepository.deleteAllPurchaseCards()
                savePurchaseCards(purchasesResponse)
                Log.d("TAG", "deleteAndSavePurchaseCards: $ids")
            }
        }
    }

    private fun savePurchaseCards(purchasesResponse: ArrayList<PurchaseCard>) {
        // save feed list into database
        launch {
            withContext(Dispatchers.IO)
            {
                val ids = dataRepository.insertPurchaseCardList(purchasesResponse)
                loadAllPurchaseCards()
                Log.d("TAG", "savePurchaseCards: $ids")
            }
        }
    }*/
}