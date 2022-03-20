package com.magma.miyyiyawmiyyi.android.presentation.home.ui.profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.magma.miyyiyawmiyyi.android.data.remote.controller.Resource
import com.magma.miyyiyawmiyyi.android.data.remote.requests.AccountRequest
import kotlinx.coroutines.CoroutineScope
import com.magma.miyyiyawmiyyi.android.data.repository.DataRepository
import com.magma.miyyiyawmiyyi.android.model.Account
import com.magma.miyyiyawmiyyi.android.utils.Event
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ProfileViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    override val coroutineContext: CoroutineContext,
) : ViewModel(), CoroutineScope {


    val validation = MutableLiveData<Event<ProfileValidation>>()

    fun onDone(accountRequest: AccountRequest) {
        //validate phone number
        when {
            accountRequest.name.isNullOrEmpty() -> {
                validation.value = Event(ProfileValidation.NAME_REQUIRED)
                return
            }
            accountRequest.birthdate.isNullOrEmpty() -> {
                validation.value = Event(ProfileValidation.BIRTH_DATE_REQUIRED)
                return
            }
            else -> {
                doServerUpdateMyAccount(accountRequest)
            }
        }
    }

    val updateResponse = MutableLiveData<Event<Resource<Account>>>()

    private fun doServerUpdateMyAccount(
        request: AccountRequest,
    ) {
        launch {
            Log.d("TAG", "doServerUpdateMyAccount: ${request.name}")
            Log.d("TAG", "doServerUpdateMyAccount: ${request.birthdate}")
            updateResponse.value = Event(Resource.Loading())
            val response: Resource<Account> =
                dataRepository.doServerUpdateMyAccount(request)
            updateResponse.value = Event(response)
        }
    }
}