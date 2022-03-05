package com.magma.miyyiyawmiyyi.android.data.remote.services

import com.magma.miyyiyawmiyyi.android.data.remote.controller.ResponseWrapper
import com.magma.miyyiyawmiyyi.android.data.remote.requests.LoginRequest
import com.magma.miyyiyawmiyyi.android.data.remote.requests.RegisterRequest
import com.magma.miyyiyawmiyyi.android.data.remote.requests.ResetPasswordRequest
import com.magma.miyyiyawmiyyi.android.data.remote.responses.LoginResponse
import com.magma.miyyiyawmiyyi.android.data.remote.responses.NearbySearchResponse
import com.magma.miyyiyawmiyyi.android.data.remote.responses.TasksResponse
import com.magma.miyyiyawmiyyi.android.utils.Urls
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

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

}