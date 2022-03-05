package com.magma.miyyiyawmiyyi.android.presentation.registration.finish_account

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import com.magma.miyyiyawmiyyi.android.data.remote.responses.LoginResponse
import com.magma.miyyiyawmiyyi.android.data.repository.DataRepository
import com.magma.miyyiyawmiyyi.android.utils.Event
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class FinishAccountViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    override val coroutineContext: CoroutineContext,
) : ViewModel(), CoroutineScope {

    val actions = MutableLiveData<Event<FinishAccountActions>>()

    fun onDone() {
        actions.value = Event(FinishAccountActions.DONE_CLICKED)
    }

    fun saveToken(loginResponse: LoginResponse) {
        loginResponse.accessToken?.token?.let { dataRepository.setApiToken(it) }
    }

}