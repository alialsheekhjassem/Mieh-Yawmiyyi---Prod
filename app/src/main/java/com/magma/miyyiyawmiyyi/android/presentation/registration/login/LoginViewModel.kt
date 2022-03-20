package com.magma.miyyiyawmiyyi.android.presentation.registration.login

import android.text.Editable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import com.magma.miyyiyawmiyyi.android.data.remote.controller.Resource
import com.magma.miyyiyawmiyyi.android.data.remote.responses.LoginResponse
import com.magma.miyyiyawmiyyi.android.data.repository.DataRepository
import com.magma.miyyiyawmiyyi.android.utils.Const
import com.magma.miyyiyawmiyyi.android.utils.Event
import com.magma.miyyiyawmiyyi.android.utils.StringRuleUtil
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class LoginViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    override val coroutineContext: CoroutineContext,
) : ViewModel(), CoroutineScope {

    val actions = MutableLiveData<Event<LoginActions>>()
    val loginValidation = MutableLiveData<Int>()
    val loginResponse = MutableLiveData<Event<Resource<LoginResponse>>>()

    fun doServerLogin(
        edtPhone: Editable,
        isAcceptTerms: Boolean
    ) {
        //validate phone number
        when {
            StringRuleUtil.NOT_EMPTY_RULE.validate(edtPhone) -> {
                loginValidation.value = Const.ERROR_EMPTY
                return
            }
            StringRuleUtil.PHONE_RULE.validate(edtPhone) -> {
                loginValidation.value = Const.ERROR_PHONE
                return
            }
            !isAcceptTerms -> {
                loginValidation.value = Const.ERROR_TERMS
                return
            }
            else -> {
                onCheckCode()
            }
        }

    }

    private fun onCheckCode() {
        actions.value = Event(LoginActions.CHECK_CODE_CLICKED)
    }

    fun saveToken(loginResponse: LoginResponse) {
        loginResponse.accessToken?.token?.let { dataRepository.setApiToken(it) }
    }

    fun getApiToken() : String? {
        return dataRepository.getApiToken()
    }

}