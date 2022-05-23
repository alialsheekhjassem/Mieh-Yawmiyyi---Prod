package com.magma.miyyiyawmiyyi.android.presentation.registration.finish_account

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.magma.miyyiyawmiyyi.android.data.remote.controller.Resource
import com.magma.miyyiyawmiyyi.android.data.remote.requests.AccountRequest
import com.magma.miyyiyawmiyyi.android.data.remote.responses.CountriesResponse
import kotlinx.coroutines.CoroutineScope
import com.magma.miyyiyawmiyyi.android.data.remote.responses.MyAccountResponse
import com.magma.miyyiyawmiyyi.android.data.repository.DataRepository
import com.magma.miyyiyawmiyyi.android.model.Account
import com.magma.miyyiyawmiyyi.android.model.Country
import com.magma.miyyiyawmiyyi.android.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class FinishAccountViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    override val coroutineContext: CoroutineContext,
) : ViewModel(), CoroutineScope {

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

    val updateResponse = MutableLiveData<Event<Resource<Account>>>()

    private fun doServerUpdateMyAccount(
        request: AccountRequest,
    ) {
        launch {
            Log.d("TAG", "doServerUpdateMyAccount: ${request.name}")
            Log.d("TAG", "doServerUpdateMyAccount: ${request.birthdate}")
            Log.d("TAG", "doServerUpdateMyAccount: ${request.firebaseFCMToken}")
            updateResponse.value = Event(Resource.Loading())
            val response: Resource<Account> =
                dataRepository.doServerUpdateMyAccount(request)
            updateResponse.value = Event(response)
        }
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
                val ids = dataRepository.deleteAllCountries()
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