package com.magma.miyyiyawmiyyi.android.data.remote.controller

import com.magma.miyyiyawmiyyi.android.data.remote.requests.AccountRequest
import com.magma.miyyiyawmiyyi.android.data.remote.requests.LoginRequest
import com.magma.miyyiyawmiyyi.android.data.remote.requests.RegisterRequest
import com.magma.miyyiyawmiyyi.android.data.remote.requests.ResetPasswordRequest
import com.magma.miyyiyawmiyyi.android.data.remote.responses.*

internal interface IRestApiManager {

    suspend fun doServerLogin(loginRequest: LoginRequest): Resource<LoginResponse>

    suspend fun doServerRegister(registerRequest: RegisterRequest): Resource<ResponseWrapper<String>>

    suspend fun doServerResetPassword(request: ResetPasswordRequest): Resource<ResponseWrapper<String>>

    suspend fun getTasks(limit: Int, offset: Int): Resource<TasksResponse>

    suspend fun getGifts(limit: Int, offset: Int): Resource<GiftStoreResponse>

    suspend fun getPurchases(limit: Int, offset: Int): Resource<GiftStorePurchasesResponse>

    suspend fun getRounds(limit: Int, offset: Int, status: String?, id: String?): Resource<RoundsResponse>

    suspend fun getMyAccount(): Resource<MyAccountResponse>

    suspend fun doServerUpdateMyAccount(accountRequest: AccountRequest): Resource<MyAccountResponse>
}