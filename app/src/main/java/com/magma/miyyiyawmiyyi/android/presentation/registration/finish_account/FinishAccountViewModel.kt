package com.magma.miyyiyawmiyyi.android.presentation.registration.finish_account

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.magma.miyyiyawmiyyi.android.data.remote.controller.Resource
import com.magma.miyyiyawmiyyi.android.data.remote.requests.AccountRequest
import kotlinx.coroutines.CoroutineScope
import com.magma.miyyiyawmiyyi.android.data.remote.responses.MyAccountResponse
import com.magma.miyyiyawmiyyi.android.data.repository.DataRepository
import com.magma.miyyiyawmiyyi.android.utils.Event
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class FinishAccountViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    override val coroutineContext: CoroutineContext,
) : ViewModel(), CoroutineScope {

    val actions = MutableLiveData<Event<FinishAccountActions>>()
    val validation = MutableLiveData<Event<FinishAccountValidation>>()

    fun onDone(accountRequest: AccountRequest) {
        //validate phone number
        when {
            accountRequest.name.isNullOrEmpty() -> {
                validation.value = Event(FinishAccountValidation.NAME_REQUIRED)
                return
            }
            accountRequest.birthdate.isNullOrEmpty() -> {
                validation.value = Event(FinishAccountValidation.BIRTH_DATE_REQUIRED)
                return
            }
            else -> {
                doServerUpdateMyAccount(accountRequest)
            }
        }
    }

    val updateResponse = MutableLiveData<Event<Resource<MyAccountResponse>>>()

    private fun doServerUpdateMyAccount(
        request: AccountRequest,
    ) {
        launch {
            Log.d("TAG", "doServerUpdateMyAccount: ${request.name}")
            Log.d("TAG", "doServerUpdateMyAccount: ${request.birthdate}")
            updateResponse.value = Event(Resource.Loading())
            val response: Resource<MyAccountResponse> =
                dataRepository.doServerUpdateMyAccount(request)
            updateResponse.value = Event(response)
        }
    }

}