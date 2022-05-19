package com.magma.miyyiyawmiyyi.android.data.remote.controller

import com.magma.miyyiyawmiyyi.android.data.remote.requests.*
import com.magma.miyyiyawmiyyi.android.data.remote.responses.*
import com.magma.miyyiyawmiyyi.android.model.Account

internal interface IRestApiManager {

    suspend fun doServerLogin(loginRequest: LoginRequest): Resource<LoginResponse>

    suspend fun doServerLogout(refreshToken: String?): Resource<Any?>

    suspend fun doServerRegister(registerRequest: RegisterRequest): Resource<ResponseWrapper<String>>

    suspend fun doServerResetPassword(request: ResetPasswordRequest): Resource<ResponseWrapper<String>>

    suspend fun getTasks(limit: Int, offset: Int, done: Boolean?, type: String?): Resource<TasksResponse>

    suspend fun generateTasks(): Resource<Any?>

    suspend fun getRoundStatistics(isActiveRound: Boolean): Resource<RoundStatisticsResponse>

    suspend fun getGifts(limit: Int, offset: Int): Resource<GiftStoreResponse>

    suspend fun getPurchases(limit: Int, offset: Int): Resource<GiftStorePurchasesResponse>

    suspend fun getNotifications(limit: Int, offset: Int): Resource<NotificationsResponse>

    suspend fun getRounds(limit: Int, offset: Int, status: String?, id: String?): Resource<RoundsResponse>

    suspend fun getMyAccount(): Resource<MyAccountResponse>

    suspend fun doServerUpdateMyAccount(accountRequest: AccountRequest): Resource<Account>

    suspend fun doServerUpdateMyAccount(accountRequest: InvitedByRequest): Resource<Account>

    suspend fun getTickets(limit: Int, offset: Int, round: String?, grandPrize: String?, populate: Boolean?): Resource<TicketsResponse>

    suspend fun getInfo(): Resource<InfoResponse>

    suspend fun doServerCreatePurchase(gift: String?): Resource<CreatePurchaseResponse>

    suspend fun doServerMarkAsDone(request: MarkAsDoneTasksRequest): Resource<Any?>

    suspend fun getAllCountries(limit: Int, offset: Int): Resource<CountriesResponse>

    suspend fun getGiftCode(id: String?): Resource<Any?>
}