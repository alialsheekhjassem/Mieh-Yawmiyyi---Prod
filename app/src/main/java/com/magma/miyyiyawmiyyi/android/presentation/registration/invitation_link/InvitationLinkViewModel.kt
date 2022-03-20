package com.magma.miyyiyawmiyyi.android.presentation.registration.invitation_link

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.magma.miyyiyawmiyyi.android.data.remote.controller.Resource
import com.magma.miyyiyawmiyyi.android.data.remote.requests.InvitedByRequest
import kotlinx.coroutines.CoroutineScope
import com.magma.miyyiyawmiyyi.android.data.remote.responses.LoginResponse
import com.magma.miyyiyawmiyyi.android.data.repository.DataRepository
import com.magma.miyyiyawmiyyi.android.model.Account
import com.magma.miyyiyawmiyyi.android.utils.Event
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class InvitationLinkViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    override val coroutineContext: CoroutineContext,
) : ViewModel(), CoroutineScope {

    val actions = MutableLiveData<Event<InvitationLinkActions>>()

    fun onSkip() {
        actions.value = Event(InvitationLinkActions.SKIP_CLICKED)
    }

    fun onDone(inviteCode: String?) {
        //validate code
        when {
            inviteCode.isNullOrEmpty() -> {
                actions.value = Event(InvitationLinkActions.EMPTY_CODE)
                return
            }
            inviteCode.length < 6 -> {
                actions.value = Event(InvitationLinkActions.WRONG_CODE)
                return
            }
            else -> {
                val request = InvitedByRequest()
                request.invitedBy = inviteCode
                doServerUpdateMyAccount(request)
            }
        }
    }

    fun onChangeNumber() {
        actions.value = Event(InvitationLinkActions.CHANGE_NUMBER_CLICKED)
    }

    fun saveToken(loginResponse: LoginResponse) {
        loginResponse.accessToken?.token?.let { dataRepository.setApiToken(it) }
    }

    val updateResponse = MutableLiveData<Event<Resource<Account>>>()

    private fun doServerUpdateMyAccount(
        request: InvitedByRequest,
    ) {
        launch {
            Log.d("TAG", "doServerUpdateMyAccount: ${request.invitedBy}")
            updateResponse.value = Event(Resource.Loading())
            val response: Resource<Account> =
                dataRepository.doServerUpdateMyAccount(request)
            updateResponse.value = Event(response)
        }
    }

}