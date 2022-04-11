package com.magma.miyyiyawmiyyi.android.data.repository

import com.magma.miyyiyawmiyyi.android.data.remote.controller.Resource
import com.magma.miyyiyawmiyyi.android.data.remote.controller.ResponseWrapper
import com.magma.miyyiyawmiyyi.android.data.remote.requests.*
import com.magma.miyyiyawmiyyi.android.data.remote.responses.*
import com.magma.miyyiyawmiyyi.android.model.*

interface DataSource {

    //Api
    suspend fun doServerLogin(loginRequest: LoginRequest): Resource<LoginResponse>
    suspend fun doServerLogout(refreshToken: String?): Resource<Any?>
    suspend fun doServerRegister(registerRequest: RegisterRequest): Resource<ResponseWrapper<String>>
    suspend fun doServerResetPassword(request: ResetPasswordRequest): Resource<ResponseWrapper<String>>
    suspend fun getTasks(limit: Int, offset: Int, done: Boolean?, type: String?): Resource<TasksResponse>
    suspend fun getGifts(limit: Int, offset: Int): Resource<GiftStoreResponse>
    suspend fun getPurchases(limit: Int, offset: Int): Resource<GiftStorePurchasesResponse>
    suspend fun getNotifications(limit: Int, offset: Int): Resource<NotificationsResponse>
    suspend fun getRounds(limit: Int, offset: Int, status: String?, id: String?): Resource<RoundsResponse>
    suspend fun getMyAccount(): Resource<MyAccountResponse>
    suspend fun doServerUpdateMyAccount(accountRequest: AccountRequest): Resource<Account>
    suspend fun doServerUpdateMyAccount(accountRequest: InvitedByRequest): Resource<Account>
    suspend fun getTickets(limit: Int, offset: Int, round: String?, populate: Boolean?): Resource<TicketsResponse>
    suspend fun getInfo(): Resource<InfoResponse>
    suspend fun doServerCreatePurchase(gift: String?): Resource<CreatePurchaseResponse>
    suspend fun doServerMarkAsDone(request: MarkAsDoneTasksRequest): Resource<Any?>
    suspend fun generateTasks(): Resource<Any?>
    suspend fun getRoundStatistics(isActiveRound: Boolean): Resource<RoundStatisticsResponse>
    suspend fun getAllCountries(limit: Int, offset: Int): Resource<CountriesResponse>
    suspend fun getGiftCode(id: String?): Resource<Any?>

    /**Local Db*/
    //Task
    fun loadAllTasks(): List<TaskObj>
    fun loadAllTasks(type: String): List<TaskObj>
    fun loadTask(id: String): TaskObj
    fun insertTaskList(items: List<TaskObj>)
    fun insertTask(item: TaskObj)
    fun updateTask(item: TaskObj)
    fun updateTasks(items: List<TaskObj>)
    fun deleteTask(item: TaskObj)
    fun deleteTask(id: String)
    fun deleteAllTasks()
    fun deleteAllTasks(type: String)
    //Gift
    fun loadAllGiftCards(): List<GiftCard>
    fun loadGiftCard(id: String): GiftCard
    fun insertGiftCardList(items: List<GiftCard>)
    fun insertGiftCard(item: GiftCard)
    fun updateGiftCard(item: GiftCard)
    fun updateGiftCards(items: List<GiftCard>)
    fun deleteGiftCard(item: GiftCard)
    fun deleteAllGiftCards()
    //Purchase
    fun loadAllPurchaseCards(): List<PurchaseCard>
    fun loadPurchaseCard(id: String): PurchaseCard
    fun insertPurchaseCardList(items: List<PurchaseCard>)
    fun insertPurchaseCard(item: PurchaseCard)
    fun updatePurchaseCard(item: PurchaseCard)
    fun updatePurchaseCards(items: List<PurchaseCard>)
    fun deletePurchaseCard(item: PurchaseCard)
    fun deleteAllPurchaseCards()
    //Ticket
    fun loadAllTickets(): List<Ticket>
    fun loadTicket(id: String): Ticket
    fun insertTicketList(items: List<Ticket>)
    fun insertTicket(item: Ticket)
    fun updateTicket(item: Ticket)
    fun updateTickets(items: List<Ticket>)
    fun deleteTicket(item: Ticket)
    fun deleteAllTickets()
    //Round
    fun loadAllRounds(): List<Round>
    fun loadRound(id: String): Round
    fun insertRoundList(items: List<Round>)
    fun insertRound(item: Round)
    fun updateRound(item: Round)
    fun updateRounds(items: List<Round>)
    fun deleteRound(item: Round)
    fun deleteAllRounds()
    //Country
    fun loadAllCountries(): List<Country>
    fun loadCountry(id: String): Country
    fun insertCountryList(items: List<Country>)
    fun insertCountry(item: Country)
    fun updateCountry(item: Country)
    fun updateCountries(items: List<Country>)
    fun deleteCountry(item: Country)
    fun deleteAllCountries()

    //Pref
    fun setGiftCode(giftCode: String)
    fun getGiftCodePref(): String?
    fun setApiToken(apiToken: String)
    fun getApiToken(): String?
    fun setRefreshToken(refreshToken: String)
    fun getRefreshToken(): String?
    fun setLang(lang: String)
    fun getLang(): String?
    fun setIsShownOnBoarding(isShown: Boolean)
    fun isShownOnBoarding(): Boolean?
    fun setIsGeneralNotifications(isGeneral: Boolean)
    fun isGeneralNotifications(): Boolean?
    fun setInvitationLink(link: String)
    fun getInvitationLink(): String?
}