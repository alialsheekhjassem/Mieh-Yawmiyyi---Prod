package com.magma.miyyiyawmiyyi.android.presentation.registration.check_code

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import com.magma.miyyiyawmiyyi.android.data.remote.controller.Resource
import com.magma.miyyiyawmiyyi.android.data.remote.requests.LoginRequest
import com.magma.miyyiyawmiyyi.android.data.remote.responses.LoginResponse
import com.magma.miyyiyawmiyyi.android.data.repository.DataRepository
import com.magma.miyyiyawmiyyi.android.utils.Event
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class CheckCodeViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    override val coroutineContext: CoroutineContext,
) : ViewModel(), CoroutineScope {

    val actions = MutableLiveData<Event<CheckCodeActions>>()
    val loginResponse = MutableLiveData<Event<Resource<LoginResponse>>>()

    fun doServerLogin(
        request: LoginRequest,
    ) {
        launch {
            Log.d("TAG", "doServerRegister: ${request.phone}")
            Log.d("TAG", "doServerRegister: ${request.token}")
            loginResponse.value = Event(Resource.Loading())
            val response: Resource<LoginResponse> =
                dataRepository.doServerLogin(request)
            loginResponse.value = Event(response)
        }
    }

    fun onEditPhoneNumber() {
        actions.value = Event(CheckCodeActions.EDIT_PHONE_CLICKED)
    }

    fun onVerify() {
        actions.value = Event(CheckCodeActions.VERIFY_CLICKED)
    }

    fun saveToken(token: String) {
        dataRepository.setApiToken(token)
    }

    fun saveRefreshToken(token: String) {
        dataRepository.setRefreshToken(token)
    }

    fun saveInvitationLink(link: String) {
        dataRepository.setInvitationLink(link)
    }

    fun onResendCode() {
        actions.value = Event(CheckCodeActions.RESEND_CODE_CLICKED)
    }

}