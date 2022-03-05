package com.magma.miyyiyawmiyyi.android.presentation.registration.invitation_link

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import com.magma.miyyiyawmiyyi.android.data.remote.responses.LoginResponse
import com.magma.miyyiyawmiyyi.android.data.repository.DataRepository
import com.magma.miyyiyawmiyyi.android.utils.Event
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

    fun onDone() {
        actions.value = Event(InvitationLinkActions.DONE_CLICKED)
    }

    fun onChangeNumber() {
        actions.value = Event(InvitationLinkActions.CHANGE_NUMBER_CLICKED)
    }

    fun saveToken(loginResponse: LoginResponse) {
        loginResponse.accessToken?.token?.let { dataRepository.setApiToken(it) }
    }

}