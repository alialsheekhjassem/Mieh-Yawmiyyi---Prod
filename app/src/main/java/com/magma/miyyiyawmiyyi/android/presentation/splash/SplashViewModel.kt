package com.magma.miyyiyawmiyyi.android.presentation.splash

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.magma.miyyiyawmiyyi.android.data.remote.controller.Resource
import com.magma.miyyiyawmiyyi.android.data.remote.responses.MyAccountResponse
import kotlinx.coroutines.CoroutineScope
import com.magma.miyyiyawmiyyi.android.data.repository.DataRepository
import com.magma.miyyiyawmiyyi.android.utils.Event
import kotlinx.coroutines.launch
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
            Log.d("TAG", "getRounds: $result")
            response.value = Event(result)
        }
    }

}