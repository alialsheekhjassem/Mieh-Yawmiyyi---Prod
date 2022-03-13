package com.magma.miyyiyawmiyyi.android.data.remote.services

import com.magma.miyyiyawmiyyi.android.data.remote.controller.ResponseWrapper
import com.magma.miyyiyawmiyyi.android.data.remote.requests.AccountRequest
import com.magma.miyyiyawmiyyi.android.data.remote.requests.LoginRequest
import com.magma.miyyiyawmiyyi.android.data.remote.requests.RegisterRequest
import com.magma.miyyiyawmiyyi.android.data.remote.requests.ResetPasswordRequest
import com.magma.miyyiyawmiyyi.android.data.remote.responses.*
import com.magma.miyyiyawmiyyi.android.utils.Urls
import retrofit2.Response
import retrofit2.http.*

interface IFoodService {

    @GET(Urls.NEARBY_PLACES)
    suspend fun getNearbyPlaces(
        @Query("type") type: String?,
        @Query("location") location: String?,
        @Query("radius") radius: Int,
        @Query("key") apiToken: String?
    ): Response<NearbySearchResponse>

    @GET(Urls.TEXT_SEARCH)
    suspend fun getPlaceDetailsByTitleAndLocation(
        @Query("query") type: String?,
        @Query("location") location: String?,
        @Query("key") apiToken: String?
    ): Response<NearbySearchResponse>

    @POST(Urls.END_POINT_LOGIN)
    suspend fun doServerLogin(
        @Body loginRequest: LoginRequest?
    ): Response<ResponseWrapper<LoginResponse>>

    @PATCH(Urls.END_POINT_UPDATE_MY_ACCOUNT)
    suspend fun doServerUpdateMyAccount(
        @Body accountRequest: AccountRequest?
    ): Response<ResponseWrapper<MyAccountResponse>>

    @POST(Urls.END_POINT_REGISTER)
    suspend fun doServerRegister(
        @Body registerRequest: RegisterRequest?
    ): Response<ResponseWrapper<String>>

    @POST(Urls.END_POINT_RESET_PASSWORD)
    suspend fun doServerResetPassword(
        @Body request: ResetPasswordRequest?
    ): Response<ResponseWrapper<String>>

    @GET(Urls.END_POINT_TASKS)
    suspend fun getTasks(
        @Query("limit") limit: Int?,
        @Query("offset") offset: Int?
    ): Response<ResponseWrapper<TasksResponse>>

    @GET(Urls.END_POINT_GIFT_STORE)
    suspend fun getGifts(
        @Query("limit") limit: Int?,
        @Query("offset") offset: Int?
    ): Response<ResponseWrapper<GiftStoreResponse>>

    @GET(Urls.END_POINT_GIFT_STORE_PURCHASES)
    suspend fun getPurchases(
        @Query("limit") limit: Int?,
        @Query("offset") offset: Int?
    ): Response<ResponseWrapper<GiftStorePurchasesResponse>>

    @GET(Urls.END_POINT_ROUNDS)
    suspend fun getRounds(
        @Query("limit") limit: Int?,
        @Query("offset") offset: Int?,
        @Query("status") status: String?,
        @Query("_id") _id: String?,
    ): Response<ResponseWrapper<RoundsResponse>>

    @GET(Urls.END_POINT_MY_ACCOUNT)
    suspend fun getMyAccount(): Response<ResponseWrapper<MyAccountResponse>>

}