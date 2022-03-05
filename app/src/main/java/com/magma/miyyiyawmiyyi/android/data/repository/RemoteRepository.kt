package com.magma.miyyiyawmiyyi.android.data.repository

import android.util.Log
import com.google.gson.Gson
import com.magma.miyyiyawmiyyi.android.data.remote.controller.*
import com.magma.miyyiyawmiyyi.android.data.remote.controller.IRestApiManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.magma.miyyiyawmiyyi.android.data.remote.requests.LoginRequest
import com.magma.miyyiyawmiyyi.android.data.remote.requests.RegisterRequest
import com.magma.miyyiyawmiyyi.android.data.remote.requests.ResetPasswordRequest
import com.magma.miyyiyawmiyyi.android.data.remote.responses.LoginResponse
import com.magma.miyyiyawmiyyi.android.data.remote.responses.TasksResponse
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