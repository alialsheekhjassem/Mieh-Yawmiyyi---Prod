package com.magma.miyyiyawmiyyi.android.data.remote.controller

import com.magma.miyyiyawmiyyi.android.data.remote.requests.LoginRequest
import com.magma.miyyiyawmiyyi.android.data.remote.requests.RegisterRequest
import com.magma.miyyiyawmiyyi.android.data.remote.requests.ResetPasswordRequest
import com.magma.miyyiyawmiyyi.android.data.remote.responses.LoginResponse
import com.magma.miyyiyawmiyyi.android.data.remote.responses.TasksResponse

internal interface IRestApiManager {

    suspend fun doServerLogin(loginRequest: LoginRequest): Resource<LoginResponse>

    suspend fun doServerRegister(registerRequest: RegisterRequest): Resource<ResponseWrapper<String>>

    suspend fun doServerResetPassword(request: ResetPasswordRequest): Resource<ResponseWrapper<String>>

    suspend fun getTasks(limit: Int, offset: Int): Resource<TasksResponse>
}