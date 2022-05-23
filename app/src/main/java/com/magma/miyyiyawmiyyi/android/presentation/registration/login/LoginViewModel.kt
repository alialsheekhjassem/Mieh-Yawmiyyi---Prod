package com.magma.miyyiyawmiyyi.android.presentation.registration.login

import android.text.Editable
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import com.magma.miyyiyawmiyyi.android.data.remote.controller.Resource
import com.magma.miyyiyawmiyyi.android.data.remote.responses.CountriesResponse
import com.magma.miyyiyawmiyyi.android.data.remote.responses.LoginResponse
import com.magma.miyyiyawmiyyi.android.data.repository.DataRepository
import com.magma.miyyiyawmiyyi.android.model.Country
import com.magma.miyyiyawmiyyi.android.utils.Const
import com.magma.miyyiyawmiyyi.android.utils.Event
import com.magma.miyyiyawmiyyi.android.utils.StringRuleUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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


    internal var responseCountries = MutableLiveData<Event<Resource<CountriesResponse>>>()
    val countriesDb = MutableLiveData<Event<List<Country>>>()

    fun getCountries(limit: Int, offset: Int) {
        launch {
            //val token = dataRepository.getApiToken()
            responseCountries.value = Event(Resource.Loading())
            val result: Resource<CountriesResponse> =
                dataRepository.getAllCountries(limit, offset)
            Log.d("TAG", "getAllCountries: $result")
            responseCountries.value = Event(result)
        }
    }

    fun loadAllCountries() {
        // save feed list into database
        launch {
            val list = withContext(Dispatchers.IO) {
                dataRepository.loadAllCountries()
            }
            Log.d("TAG", "loadAllCountries: $list")
            countriesDb.value = Event(list)
        }
    }

    fun deleteAndSaveCountries(countryList: ArrayList<Country>) {
        // save feed list into database
        launch {
            withContext(Dispatchers.IO)
            {
                val ids = dataRepository.deleteAllPurchaseCards()
                saveCountries(countryList)
                Log.d("TAG", "deleteAndSaveCountries: $ids")
            }
        }
    }

    private fun saveCountries(countryList: ArrayList<Country>) {
        // save feed list into database
        launch {
            withContext(Dispatchers.IO)
            {
                val ids = dataRepository.insertCountryList(countryList)
                //loadAllCountries()
                Log.d("TAG", "saveCountries: $ids")
            }
        }
    }

}