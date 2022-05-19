package com.magma.miyyiyawmiyyi.android.presentation.splash

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.magma.miyyiyawmiyyi.android.data.remote.controller.Resource
import com.magma.miyyiyawmiyyi.android.data.remote.responses.InfoResponse
import com.magma.miyyiyawmiyyi.android.data.remote.responses.MyAccountResponse
import com.magma.miyyiyawmiyyi.android.data.remote.responses.RoundsResponse
import kotlinx.coroutines.CoroutineScope
import com.magma.miyyiyawmiyyi.android.data.repository.DataRepository
import com.magma.miyyiyawmiyyi.android.model.Round
import com.magma.miyyiyawmiyyi.android.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class SplashViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    override val coroutineContext: CoroutineContext,
) : ViewModel(), CoroutineScope {

    fun isShowOnBoarding(): Boolean {
        return dataRepository.isShownOnBoarding()
    }

    fun getToken(): String? {
        return dataRepository.getApiToken()
    }

    fun setApiToken(token: String) {
        return dataRepository.setApiToken(token)
    }

    fun setRefreshToken(token: String) {
        return dataRepository.setRefreshToken(token)
    }

    fun getLang(): String {
        return dataRepository.getLang()?:"ar"
    }

    fun getIsEnableAds(): Boolean {
        return dataRepository.isEnableAds()
    }

    fun setIsEnableAds(isEnableAds: Boolean) {
        dataRepository.setIsEnableAds(isEnableAds)
    }

    fun setIsShowQuizTask(isShown: Boolean) {
        dataRepository.setIsShowQuizTask(isShown)
    }

    fun setIsShowSocialMediaTask(isShown: Boolean) {
        dataRepository.setIsShowSocialMediaTask(isShown)
    }

    fun setIsShowAdTask(isShown: Boolean) {
        dataRepository.setIsShowAdTask(isShown)
    }

    /**
     * Live data of requests list response.
     * */
    internal var response = MutableLiveData<Event<Resource<MyAccountResponse>>>()

    fun getMyAccount() {
        launch {
            //val token = dataRepository.getApiToken()
            response.value = Event(Resource.Loading())
            val result: Resource<MyAccountResponse> =
                dataRepository.getMyAccount()
            Log.d("TAG", "getMyAccount: $result")
            response.value = Event(result)
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

    /**
     * Live data of requests list response.
     * */
    internal var roundResponse = MutableLiveData<Event<Resource<RoundsResponse>>>()

    fun getRounds(limit: Int, offset: Int, status: String?, id: String?) {
        launch {
            //val token = dataRepository.getApiToken()
            roundResponse.value = Event(Resource.Loading())
            val result: Resource<RoundsResponse> =
                dataRepository.getRounds(limit, offset, status, id)
            Log.d("TAG", "getRounds: $result")
            roundResponse.value = Event(result)
        }
    }

    fun deleteAndSaveRounds(roundsResponse: ArrayList<Round>) {
        // save feed list into database
        launch {
            withContext(Dispatchers.IO)
            {
                val ids = dataRepository.deleteAllRounds()
                saveRounds(roundsResponse)
                Log.d("TAG", "deleteAndSaveRounds: $ids")
            }
        }
    }

    private fun saveRounds(roundsResponse: ArrayList<Round>) {
        // save feed list into database
        launch {
            withContext(Dispatchers.IO)
            {
                val ids = dataRepository.insertRoundList(roundsResponse)
                Log.d("TAG", "saveTickets: $ids")
            }
        }
    }

    /**
     * Live data of logout
     * */
    internal var logoutResponse = MutableLiveData<Event<Resource<Any?>>>()

    fun doServerLogout() {
        launch {
            //val token = dataRepository.getApiToken()
            logoutResponse.value = Event(Resource.Loading())
            val result: Resource<Any?> =
                dataRepository.doServerLogout("Bearer ${dataRepository.getRefreshToken()}")
            Log.d("TAG", "doServerLogout: $result")
            logoutResponse.value = Event(result)
        }
    }
}