package com.magma.miyyiyawmiyyi.android.data.repository

import android.util.Log
import com.google.gson.Gson
import com.magma.miyyiyawmiyyi.android.data.remote.controller.*
import com.magma.miyyiyawmiyyi.android.data.remote.controller.IRestApiManager
import com.magma.miyyiyawmiyyi.android.data.remote.requests.AccountRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.magma.miyyiyawmiyyi.android.data.remote.requests.LoginRequest
import com.magma.miyyiyawmiyyi.android.data.remote.requests.RegisterRequest
import com.magma.miyyiyawmiyyi.android.data.remote.requests.ResetPasswordRequest
import com.magma.miyyiyawmiyyi.android.data.remote.responses.*
import com.magma.miyyiyawmiyyi.android.data.remote.services.IFoodService
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class RemoteRepository
@Inject constructor(private val serviceGenerator: ServiceGenerator, private val gson: Gson) :
    IRestApiManager {

    override suspend fun doServerLogin(loginRequest: LoginRequest): Resource<LoginResponse> {
        val authService = serviceGenerator.createService(IFoodService::class.java)
        try {
            val response = authService.doServerLogin(loginRequest)

            return if (response.isSuccessful) {
                //Do something with response e.g show to the UI.
                val loginResponse = response.body()?.successResult as LoginResponse
                Log.d(TAG, "doServerLogin: isSuccessful " + response.code())
                Log.d(TAG, "doServerLogin: isSuccessful $loginResponse")
                Resource.Success(loginResponse)
            } else {
                Log.d(TAG, "doServerLogin: isSuccessful no " + response.code())
                Log.d(TAG, "doServerLogin: isSuccessful no " + response.message())
                val errorBody = gson.fromJson(
                    response.errorBody()?.stringSuspending(),
                    ErrorManager::class.java
                )
                Resource.DataError(errorBody)
            }
        } catch (e: HttpException) {
            return Resource.Exception(e.message() as String)
        } catch (e: Throwable) {
            return Resource.Exception(errorMessage = e.message as String)
        } catch (e: SocketTimeoutException) {
            return Resource.Exception(errorMessage = e.message as String)
        } catch (e: IOException) {
            return Resource.Exception(errorMessage = e.message as String)
        }
    }

    override suspend fun doServerRegister(registerRequest: RegisterRequest): Resource<ResponseWrapper<String>> {
        val authService = serviceGenerator.createService(IFoodService::class.java)
        try {
            val response = authService.doServerRegister(registerRequest)

            return if (response.isSuccessful) {
                //Do something with response e.g show to the UI.
                val registerResponse = response.body() as ResponseWrapper<String>
                Log.d(TAG, "doServerRegister: isSuccessful " + response.code())
                Log.d(TAG, "doServerRegister: isSuccessful $registerResponse")
                Resource.Success(registerResponse)
            } else {
                Log.d(TAG, "doServerRegister: isSuccessful no " + response.code())
                Log.d(TAG, "doServerRegister: isSuccessful no " + response.message())
                val errorBody = gson.fromJson(
                    response.errorBody()?.stringSuspending(),
                    ErrorManager::class.java
                )
                Resource.DataError(errorBody)
            }
        } catch (e: HttpException) {
            return Resource.Exception(e.message() as String)
        } catch (e: Throwable) {
            return Resource.Exception(errorMessage = e.message as String)
        } catch (e: SocketTimeoutException) {
            return Resource.Exception(errorMessage = e.message as String)
        } catch (e: IOException) {
            return Resource.Exception(errorMessage = e.message as String)
        }
    }

    override suspend fun doServerResetPassword(request: ResetPasswordRequest): Resource<ResponseWrapper<String>> {
        val authService = serviceGenerator.createService(IFoodService::class.java)
        try {
            val response = authService.doServerResetPassword(request)

            return if (response.isSuccessful) {
                //Do something with response e.g show to the UI.
                val registerResponse = response.body() as ResponseWrapper<String>
                Log.d(TAG, "doServerRegister: isSuccessful " + response.code())
                Log.d(TAG, "doServerRegister: isSuccessful $registerResponse")
                Resource.Success(registerResponse)
            } else {
                Log.d(TAG, "doServerRegister: isSuccessful no " + response.code())
                Log.d(TAG, "doServerRegister: isSuccessful no " + response.message())
                val errorBody = gson.fromJson(
                    response.errorBody()?.stringSuspending(),
                    ErrorManager::class.java
                )
                Resource.DataError(errorBody)
            }
        } catch (e: HttpException) {
            return Resource.Exception(e.message() as String)
        } catch (e: Throwable) {
            return Resource.Exception(errorMessage = e.message as String)
        } catch (e: SocketTimeoutException) {
            return Resource.Exception(errorMessage = e.message as String)
        } catch (e: IOException) {
            return Resource.Exception(errorMessage = e.message as String)
        }
    }

    override suspend fun getGifts(limit: Int, offset: Int): Resource<GiftStoreResponse> {
        val authService = serviceGenerator.createService(IFoodService::class.java)
        try {
            val response = authService.getGifts(limit, offset)

            return if (response.isSuccessful) {
                //Do something with response e.g show to the UI.
                val giftsResponse = response.body()?.successResult as GiftStoreResponse
                Log.d(TAG, "getGifts: isSuccessful " + response.code())
                Log.d(TAG, "getGifts: isSuccessful $giftsResponse")
                Resource.Success(giftsResponse)
            } else {
                Log.d(TAG, "getGifts: isSuccessful no " + response.code())
                Log.d(TAG, "getGifts: isSuccessful no " + response.message())
                val errorBody = gson.fromJson(
                    response.errorBody()?.stringSuspending(),
                    ErrorManager::class.java
                )
                Resource.DataError(errorBody)
            }
        } catch (e: HttpException) {
            return Resource.Exception(e.message() as String)
        } catch (e: Throwable) {
            return Resource.Exception(errorMessage = e.message as String)
        } catch (e: SocketTimeoutException) {
            return Resource.Exception(errorMessage = e.message as String)
        } catch (e: IOException) {
            return Resource.Exception(errorMessage = e.message as String)
        }
    }

    override suspend fun getPurchases(
        limit: Int,
        offset: Int
    ): Resource<GiftStorePurchasesResponse> {
        val authService = serviceGenerator.createService(IFoodService::class.java)
        try {
            val response = authService.getPurchases(limit, offset)

            return if (response.isSuccessful) {
                //Do something with response e.g show to the UI.
                val purchasesResponse = response.body()?.successResult as GiftStorePurchasesResponse
                Log.d(TAG, "getPurchases: isSuccessful " + response.code())
                Log.d(TAG, "getPurchases: isSuccessful $purchasesResponse")
                Resource.Success(purchasesResponse)
            } else {
                Log.d(TAG, "getPurchases: isSuccessful no " + response.code())
                Log.d(TAG, "getPurchases: isSuccessful no " + response.message())
                val errorBody = gson.fromJson(
                    response.errorBody()?.stringSuspending(),
                    ErrorManager::class.java
                )
                Resource.DataError(errorBody)
            }
        } catch (e: HttpException) {
            return Resource.Exception(e.message() as String)
        } catch (e: Throwable) {
            return Resource.Exception(errorMessage = e.message as String)
        } catch (e: SocketTimeoutException) {
            return Resource.Exception(errorMessage = e.message as String)
        } catch (e: IOException) {
            return Resource.Exception(errorMessage = e.message as String)
        }
    }

    override suspend fun getRounds(limit: Int, offset: Int, status: String?, id: String?): Resource<RoundsResponse> {
        val authService = serviceGenerator.createService(IFoodService::class.java)
        try {
            val response = authService.getRounds(limit, offset, status, id)

            return if (response.isSuccessful) {
                //Do something with response e.g show to the UI.
                val purchasesResponse = response.body()?.successResult as RoundsResponse
                Log.d(TAG, "getPurchases: isSuccessful " + response.code())
                Log.d(TAG, "getPurchases: isSuccessful $purchasesResponse")
                Resource.Success(purchasesResponse)
            } else {
                Log.d(TAG, "getPurchases: isSuccessful no " + response.code())
                Log.d(TAG, "getPurchases: isSuccessful no " + response.message())
                val errorBody = gson.fromJson(
                    response.errorBody()?.stringSuspending(),
                    ErrorManager::class.java
                )
                Resource.DataError(errorBody)
            }
        } catch (e: HttpException) {
            return Resource.Exception(e.message() as String)
        } catch (e: Throwable) {
            return Resource.Exception(errorMessage = e.message as String)
        } catch (e: SocketTimeoutException) {
            return Resource.Exception(errorMessage = e.message as String)
        } catch (e: IOException) {
            return Resource.Exception(errorMessage = e.message as String)
        }
    }

    override suspend fun getMyAccount(): Resource<MyAccountResponse> {
        val authService = serviceGenerator.createService(IFoodService::class.java)
        try {
            val response = authService.getMyAccount()

            return if (response.isSuccessful) {
                //Do something with response e.g show to the UI.
                val purchasesResponse = response.body()?.successResult as MyAccountResponse
                Log.d(TAG, "getPurchases: isSuccessful " + response.code())
                Log.d(TAG, "getPurchases: isSuccessful $purchasesResponse")
                Resource.Success(purchasesResponse)
            } else {
                Log.d(TAG, "getPurchases: isSuccessful no " + response.code())
                Log.d(TAG, "getPurchases: isSuccessful no " + response.message())
                val errorBody = gson.fromJson(
                    response.errorBody()?.stringSuspending(),
                    ErrorManager::class.java
                )
                Resource.DataError(errorBody)
            }
        } catch (e: HttpException) {
            return Resource.Exception(e.message() as String)
        } catch (e: Throwable) {
            return Resource.Exception(errorMessage = e.message as String)
        } catch (e: SocketTimeoutException) {
            return Resource.Exception(errorMessage = e.message as String)
        } catch (e: IOException) {
            return Resource.Exception(errorMessage = e.message as String)
        }
    }

    override suspend fun doServerUpdateMyAccount(accountRequest: AccountRequest): Resource<MyAccountResponse> {
        val authService = serviceGenerator.createService(IFoodService::class.java)
        try {
            val response = authService.doServerUpdateMyAccount(accountRequest)

            return if (response.isSuccessful) {
                //Do something with response e.g show to the UI.
                val updateResponse = response.body()?.successResult as MyAccountResponse
                Log.d(TAG, "doServerUpdateMyAccount: isSuccessful " + response.code())
                Log.d(TAG, "doServerUpdateMyAccount: isSuccessful $updateResponse")
                Resource.Success(updateResponse)
            } else {
                Log.d(TAG, "doServerUpdateMyAccount: isSuccessful no " + response.code())
                Log.d(TAG, "doServerUpdateMyAccount: isSuccessful no " + response.message())
                val errorBody = gson.fromJson(
                    response.errorBody()?.stringSuspending(),
                    ErrorManager::class.java
                )
                Resource.DataError(errorBody)
            }
        } catch (e: HttpException) {
            return Resource.Exception(e.message() as String)
        } catch (e: Throwable) {
            return Resource.Exception(errorMessage = e.message as String)
        } catch (e: SocketTimeoutException) {
            return Resource.Exception(errorMessage = e.message as String)
        } catch (e: IOException) {
            return Resource.Exception(errorMessage = e.message as String)
        }
    }

    override suspend fun getTasks(limit: Int, offset: Int): Resource<TasksResponse> {
        val authService = serviceGenerator.createService(IFoodService::class.java)
        try {
            val response = authService.getTasks(limit, offset)

            return if (response.isSuccessful) {
                //Do something with response e.g show to the UI.
                val tasksResponse = response.body()?.successResult as TasksResponse
                Log.d(TAG, "getTasks: isSuccessful " + response.code())
                Log.d(TAG, "getTasks: isSuccessful $tasksResponse")
                Resource.Success(tasksResponse)
            } else {
                Log.d(TAG, "getTasks: isSuccessful no " + response.code())
                Log.d(TAG, "getTasks: isSuccessful no " + response.message())
                val errorBody = gson.fromJson(
                    response.errorBody()?.stringSuspending(),
                    ErrorManager::class.java
                )
                Resource.DataError(errorBody)
            }
        } catch (e: HttpException) {
            return Resource.Exception(e.message() as String)
        } catch (e: Throwable) {
            return Resource.Exception(errorMessage = e.message as String)
        } catch (e: SocketTimeoutException) {
            return Resource.Exception(errorMessage = e.message as String)
        } catch (e: IOException) {
            return Resource.Exception(errorMessage = e.message as String)
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun ResponseBody.stringSuspending() =
        withContext(Dispatchers.IO) { string() }

    companion object {
        private const val TAG = "RemoteRepository"
    }

}