package com.magma.miyyiyawmiyyi.android.data.remote.services

import com.magma.miyyiyawmiyyi.android.data.remote.controller.ResponseWrapper
import com.magma.miyyiyawmiyyi.android.data.remote.requests.*
import com.magma.miyyiyawmiyyi.android.data.remote.responses.*
import com.magma.miyyiyawmiyyi.android.model.Account
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

    @POST(Urls.END_POINT_LOGOUT)
    suspend fun doServerLogout(): Response<ResponseWrapper<Any?>>

    @PATCH(Urls.END_POINT_UPDATE_MY_ACCOUNT)
    suspend fun doServerUpdateMyAccount(
        @Body accountRequest: AccountRequest?
    ): Response<ResponseWrapper<Account>>

    @PATCH(Urls.END_POINT_UPDATE_MY_ACCOUNT)
    suspend fun doServerUpdateMyAccount(
        @Body accountRequest: InvitedByRequest?
    ): Response<ResponseWrapper<Account>>

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

    @GET(Urls.END_POINT_INBOX)
    suspend fun getNotifications(
        @Query("limit") limit: Int?,
        @Query("offset") offset: Int?
    ): Response<ResponseWrapper<NotificationsResponse>>

    @GET(Urls.END_POINT_ROUNDS)
    suspend fun getRounds(
        @Query("limit") limit: Int?,
        @Query("offset") offset: Int?,
        @Query("status") status: String?,
        @Query("_id") _id: String?,
    ): Response<ResponseWrapper<RoundsResponse>>

    @GET(Urls.END_POINT_MY_ACCOUNT)
    suspend fun getMyAccount(): Response<ResponseWrapper<MyAccountResponse>>

    @GET(Urls.END_POINT_TICKETS)
    suspend fun getTickets(
        @Query("limit") limit: Int?,
        @Query("offset") offset: Int?,
        @Query("round") round: String?,
        @Query("populate") populate: Boolean?,
    ): Response<ResponseWrapper<TicketsResponse>>

    @GET(Urls.END_POINT_INFO)
    suspend fun getInfo(): Response<ResponseWrapper<InfoResponse>>

    @POST(Urls.END_POINT_CREATE_PURCHASE)
    suspend fun doServerCreatePurchase(
        @Path("gift") gift: String?,
    ): Response<ResponseWrapper<CreatePurchaseResponse>>

    @PATCH(Urls.END_POINT_MARK_AS_DONE)
    suspend fun doServerMarkAsDoneTasks(
        @Body request: MarkAsDoneTasksRequest,
    ): Response<ResponseWrapper<Any?>>

}