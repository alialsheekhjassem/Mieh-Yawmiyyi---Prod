package com.magma.miyyiyawmiyyi.android.presentation.home.ui.our_store

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.magma.miyyiyawmiyyi.android.data.remote.controller.Resource
import com.magma.miyyiyawmiyyi.android.data.remote.responses.CreatePurchaseResponse
import com.magma.miyyiyawmiyyi.android.data.remote.responses.GiftStorePurchasesResponse
import com.magma.miyyiyawmiyyi.android.data.remote.responses.GiftStoreResponse
import kotlinx.coroutines.CoroutineScope
import com.magma.miyyiyawmiyyi.android.data.repository.DataRepository
import com.magma.miyyiyawmiyyi.android.model.GiftCard
import com.magma.miyyiyawmiyyi.android.model.PurchaseCard
import com.magma.miyyiyawmiyyi.android.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class OurStoreViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    override val coroutineContext: CoroutineContext,
) : ViewModel(), CoroutineScope {
    fun getLanguage(): String? {
        return dataRepository.getLang()
    }

    fun setLanguage(lang: String) {
        dataRepository.setLang(lang)
    }

    /**
     * Live data of requests list response.
     * */
    internal var response = MutableLiveData<Event<Resource<GiftStoreResponse>>>()
    val giftStoreCardsDb = MutableLiveData<Event<List<GiftCard>>>()
    val actions = MutableLiveData<Event<OurStoreActions>>()

    internal var responsePurchases = MutableLiveData<Event<Resource<GiftStorePurchasesResponse>>>()
    val purchasesCardsDb = MutableLiveData<Event<List<PurchaseCard>>>()

    fun onQuizzesClicked() {
        actions.value = Event(OurStoreActions.QUIZZES_CLICKED)
    }

    fun getGiftStore(limit: Int, offset: Int) {
        launch {
            //val token = dataRepository.getApiToken()
            response.value = Event(Resource.Loading())
            val result: Resource<GiftStoreResponse> =
                dataRepository.getGifts(limit, offset)
            Log.d("TAG", "getGiftStore: $result")
            response.value = Event(result)
        }
    }

    fun loadAllGiftStoreCards() {
        // save feed list into database
        launch {
            val list = withContext(Dispatchers.IO) {
                dataRepository.loadAllGiftCards()
            }
            Log.d("TAG", "loadAllGiftStoreCards: $list")
            giftStoreCardsDb.value = Event(list)
        }
    }

    fun deleteAndSaveGiftStoreCards(response: GiftStoreResponse) {
        // save feed list into database
        launch {
            withContext(Dispatchers.IO)
            {
                val items = dataRepository.loadAllGiftCards()
                deleteAndSaveGiftStoreCards(response.items)
                Log.d("TAG", "deleteAndSaveGiftStoreCards: $items")
            }
        }
    }

    private fun deleteAndSaveGiftStoreCards(giftsResponse: ArrayList<GiftCard>) {
        // save feed list into database
        launch {
            withContext(Dispatchers.IO)
            {
                val ids = dataRepository.deleteAllGiftCards()
                saveGiftStoreCards(giftsResponse)
                Log.d("TAG", "deleteAndSaveGiftStoreCards: $ids")
            }
        }
    }

    private fun saveGiftStoreCards(giftsResponse: ArrayList<GiftCard>) {
        // save feed list into database
        launch {
            withContext(Dispatchers.IO)
            {
                val ids = dataRepository.insertGiftCardList(giftsResponse)
                loadAllGiftStoreCards()
                Log.d("TAG", "saveGiftStoreCards: $ids")
            }
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

    internal var responseCreatePurchase = MutableLiveData<Event<Resource<CreatePurchaseResponse>>>()
    fun doServerCreatePurchase(gift: String?) {
        launch {
            //val token = dataRepository.getApiToken()
            responseCreatePurchase.value = Event(Resource.Loading())
            val result: Resource<CreatePurchaseResponse> =
                dataRepository.doServerCreatePurchase(gift)
            Log.d("TAG", "doServerCreatePurchase: $result")
            responseCreatePurchase.value = Event(result)
        }
    }

}