package com.magma.miyyiyawmiyyi.android.presentation.home.ui.orders

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.magma.miyyiyawmiyyi.android.data.remote.controller.Resource
import com.magma.miyyiyawmiyyi.android.data.remote.responses.GiftStorePurchasesResponse
import kotlinx.coroutines.CoroutineScope
import com.magma.miyyiyawmiyyi.android.data.repository.DataRepository
import com.magma.miyyiyawmiyyi.android.model.PurchaseCard
import com.magma.miyyiyawmiyyi.android.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class OrdersViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    override val coroutineContext: CoroutineContext,
) : ViewModel(), CoroutineScope {

    internal var responsePurchases = MutableLiveData<Event<Resource<GiftStorePurchasesResponse>>>()
    internal var responseCode = MutableLiveData<Event<Resource<Any?>>>()
    val purchasesCardsDb = MutableLiveData<Event<List<PurchaseCard>>>()

    fun getGiftCodePref(): String?{
        return dataRepository.getGiftCodePref()
    }

    fun getGiftCode(id: String) {
        launch {
            //val token = dataRepository.getApiToken()
            responseCode.value = Event(Resource.Loading())
            val result: Resource<Any?> =
                dataRepository.getGiftCode(id)
            Log.d("TAG", "getGiftCode: $result")
            responseCode.value = Event(result)
        }
    }

    fun getPurchaseCards(limit: Int, offset: Int) {
        launch {
            //val token = dataRepository.getApiToken()
            responsePurchases.value = Event(Resource.Loading())
            val result: Resource<GiftStorePurchasesResponse> =
                dataRepository.getPurchases(limit, offset)
            Log.d("TAG", "getPurchaseCards: $result")
            responsePurchases.value = Event(result)
        }
    }

    fun loadAllPurchaseCards() {
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
    }
}