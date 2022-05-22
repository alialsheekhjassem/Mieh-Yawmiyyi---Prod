package com.magma.miyyiyawmiyyi.android.data.repository

import com.magma.miyyiyawmiyyi.android.data.local.repository.LocalRepository
import com.magma.miyyiyawmiyyi.android.data.remote.controller.Resource
import com.magma.miyyiyawmiyyi.android.data.remote.controller.ResponseWrapper
import com.magma.miyyiyawmiyyi.android.data.remote.requests.*
import com.magma.miyyiyawmiyyi.android.data.remote.responses.*
import com.magma.miyyiyawmiyyi.android.model.*
import javax.inject.Inject

class DataRepository
@Inject
constructor(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository
) : DataSource {

    //Api
    override suspend fun doServerLogin(loginRequest: LoginRequest): Resource<LoginResponse> {
        return remoteRepository.doServerLogin(loginRequest)
    }

    override suspend fun doServerLogout(refreshToken: String?): Resource<Any?> {
        return remoteRepository.doServerLogout(refreshToken)
    }

    override suspend fun doServerRegister(registerRequest: RegisterRequest): Resource<ResponseWrapper<String>> {
        return remoteRepository.doServerRegister(registerRequest)
    }

    override suspend fun doServerResetPassword(request: ResetPasswordRequest): Resource<ResponseWrapper<String>> {
        return remoteRepository.doServerResetPassword(request)
    }

    override suspend fun getTasks(limit: Int, offset: Int, done: Boolean?, type: String?): Resource<TasksResponse> {
        return remoteRepository.getTasks(limit, offset, done, type)
    }

    override suspend fun getGroupedTasks(limit: Int): Resource<ArrayList<GroupedTasksResponse>> {
        return remoteRepository.getGroupedTasks(limit)
    }

    override suspend fun getGifts(limit: Int, offset: Int): Resource<GiftStoreResponse> {
        return remoteRepository.getGifts(limit, offset)
    }

    override suspend fun getPurchases(
        limit: Int,
        offset: Int
    ): Resource<GiftStorePurchasesResponse> {
        return remoteRepository.getPurchases(limit, offset)
    }

    override suspend fun getNotifications(
        limit: Int,
        offset: Int
    ): Resource<NotificationsResponse> {
        return remoteRepository.getNotifications(limit, offset)
    }

    override suspend fun getRounds(limit: Int, offset: Int, status: String?, id: String?): Resource<RoundsResponse> {
        return remoteRepository.getRounds(limit, offset, status, id)
    }

    override suspend fun getMyAccount(): Resource<MyAccountResponse> {
        return remoteRepository.getMyAccount()
    }

    override suspend fun doServerUpdateMyAccount(accountRequest: AccountRequest): Resource<Account> {
        return remoteRepository.doServerUpdateMyAccount(accountRequest)
    }

    override suspend fun doServerUpdateMyAccount(accountRequest: InvitedByRequest): Resource<Account> {
        return remoteRepository.doServerUpdateMyAccount(accountRequest)
    }

    override suspend fun getTickets(
        limit: Int,
        offset: Int,
        round: String?,
        grandPrize: String?,
        populate: Boolean?
    ): Resource<TicketsResponse> {
        return remoteRepository.getTickets(limit, offset, round, grandPrize, populate)
    }

    override suspend fun getInfo(): Resource<InfoResponse> {
        return remoteRepository.getInfo()
    }

    override suspend fun doServerCreatePurchase(gift: String?): Resource<CreatePurchaseResponse> {
        return remoteRepository.doServerCreatePurchase(gift)
    }

    override suspend fun doServerMarkAsDone(request: MarkAsDoneTasksRequest): Resource<Any?> {
        return remoteRepository.doServerMarkAsDone(request)
    }

    override suspend fun generateTasks(): Resource<Any?> {
        return remoteRepository.generateTasks()
    }

    override suspend fun getRoundStatistics(isActiveRound: Boolean): Resource<RoundStatisticsResponse> {
        return remoteRepository.getRoundStatistics(isActiveRound)
    }

    override suspend fun getAllCountries(limit: Int, offset: Int): Resource<CountriesResponse> {
        return remoteRepository.getAllCountries(limit, offset)
    }

    override suspend fun getGiftCode(id: String?): Resource<Any?> {
        return remoteRepository.getGiftCode(id)
    }

    override fun loadAllTasks(): List<TaskObj> {
        return localRepository.loadAllTasks()
    }

    override fun loadAllTasks(type: String): List<TaskObj> {
        return localRepository.loadAllTasks(type)
    }

    override fun loadTask(id: String): TaskObj {
        return localRepository.loadTask(id)
    }

    override fun insertTaskList(items: List<TaskObj>) {
        localRepository.insertTaskList(items)
    }

    override fun insertTask(item: TaskObj) {
        localRepository.insertTask(item)
    }

    override fun updateTask(item: TaskObj) {
        localRepository.updateTask(item)
    }

    override fun updateTasks(items: List<TaskObj>) {
        localRepository.updateTasks(items)
    }

    override fun deleteTask(item: TaskObj) {
        localRepository.deleteTask(item)
    }

    override fun deleteTask(id: String) {
        localRepository.deleteTask(id)
    }

    override fun deleteAllTasks() {
        localRepository.deleteAllTasks()
    }

    override fun deleteAllTasks(type: String) {
        localRepository.deleteAllTasks(type)
    }

    override fun loadAllGiftCards(): List<GiftCard> {
        return localRepository.loadAllGiftCards()
    }

    override fun loadGiftCard(id: String): GiftCard {
        return localRepository.loadGiftCard(id)
    }

    override fun insertGiftCardList(items: List<GiftCard>) {
        localRepository.insertGiftCardList(items)
    }

    override fun insertGiftCard(item: GiftCard) {
        localRepository.insertGiftCard(item)
    }

    override fun updateGiftCard(item: GiftCard) {
        localRepository.updateGiftCard(item)
    }

    override fun updateGiftCards(items: List<GiftCard>) {
        localRepository.updateGiftCards(items)
    }

    override fun deleteGiftCard(item: GiftCard) {
        localRepository.deleteGiftCard(item)
    }

    override fun deleteAllGiftCards() {
        localRepository.deleteAllGiftCards()
    }

    override fun loadAllPurchaseCards(): List<PurchaseCard> {
        return localRepository.loadAllPurchaseCards()
    }

    override fun loadPurchaseCard(id: String): PurchaseCard {
        return localRepository.loadPurchaseCard(id)
    }

    override fun insertPurchaseCardList(items: List<PurchaseCard>) {
        localRepository.insertPurchaseCardList(items)
    }

    override fun insertPurchaseCard(item: PurchaseCard) {
        localRepository.insertPurchaseCard(item)
    }

    override fun updatePurchaseCard(item: PurchaseCard) {
        localRepository.updatePurchaseCard(item)
    }

    override fun updatePurchaseCards(items: List<PurchaseCard>) {
        localRepository.updatePurchaseCards(items)
    }

    override fun deletePurchaseCard(item: PurchaseCard) {
        localRepository.deletePurchaseCard(item)
    }

    override fun deleteAllPurchaseCards() {
        localRepository.deleteAllPurchaseCards()
    }

    override fun loadAllTickets(): List<Ticket> {
        return localRepository.loadAllTickets()
    }

    override fun loadTicket(id: String): Ticket {
        return localRepository.loadTicket(id)
    }

    override fun insertTicketList(items: List<Ticket>) {
        localRepository.insertTicketList(items)
    }

    override fun insertTicket(item: Ticket) {
        localRepository.insertTicket(item)
    }

    override fun updateTicket(item: Ticket) {
        localRepository.updateTicket(item)
    }

    override fun updateTickets(items: List<Ticket>) {
        localRepository.updateTickets(items)
    }

    override fun deleteTicket(item: Ticket) {
        localRepository.deleteTicket(item)
    }

    override fun deleteAllTickets() {
        localRepository.deleteAllTickets()
    }

    override fun loadAllRounds(): List<Round> {
        return localRepository.loadAllRounds()
    }

    override fun loadRound(id: String): Round {
        return localRepository.loadRound(id)
    }

    override fun insertRoundList(items: List<Round>) {
        localRepository.insertRoundList(items)
    }

    override fun insertRound(item: Round) {
        localRepository.insertRound(item)
    }

    override fun updateRound(item: Round) {
        localRepository.updateRound(item)
    }

    override fun updateRounds(items: List<Round>) {
        localRepository.updateRounds(items)
    }

    override fun deleteRound(item: Round) {
        localRepository.deleteRound(item)
    }

    override fun deleteAllRounds() {
        localRepository.deleteAllRounds()
    }

    override fun loadAllCountries(): List<Country> {
        return localRepository.loadAllCountries()
    }

    override fun loadCountry(id: String): Country {
        return localRepository.loadCountry(id)
    }

    override fun insertCountryList(items: List<Country>) {
        localRepository.insertCountryList(items)
    }

    override fun insertCountry(item: Country) {
        localRepository.insertCountry(item)
    }

    override fun updateCountry(item: Country) {
        localRepository.updateCountry(item)
    }

    override fun updateCountries(items: List<Country>) {
        localRepository.updateCountries(items)
    }

    override fun deleteCountry(item: Country) {
        localRepository.deleteCountry(item)
    }

    override fun deleteAllCountries() {
        localRepository.deleteAllCountries()
    }


    //Pref

    override fun setGiftCode(giftCode: String) {
        localRepository.setGiftCode(giftCode)
    }

    override fun getGiftCodePref(): String? {
        return localRepository.getGiftCode()
    }

    override fun setApiToken(apiToken: String) {
        localRepository.setApiToken(apiToken)
    }

    override fun getApiToken(): String? {
        return localRepository.getApiToken()
    }

    override fun setRefreshToken(refreshToken: String) {
        localRepository.setRefreshToken(refreshToken)
    }

    override fun getRefreshToken(): String? {
        return localRepository.getRefreshToken()
    }

    override fun setLang(lang: String) {
        localRepository.setLang(lang)
    }

    override fun getLang(): String? {
        return localRepository.getLang()
    }

    override fun setIsShownOnBoarding(isShown: Boolean) {
        localRepository.setIsShownOnBoarding(isShown)
    }

    override fun isShownOnBoarding(): Boolean {
        return localRepository.isShownOnBoarding()
    }

    override fun setIsGeneralNotifications(isGeneral: Boolean) {
        localRepository.setIsGeneralNotifications(isGeneral)
    }

    override fun isGeneralNotifications(): Boolean {
        return localRepository.isGeneralNotifications()
    }

    override fun setInvitationLink(link: String) {
        localRepository.setInvitationLink(link)
    }

    override fun getInvitationLink(): String? {
        return localRepository.getInvitationLink()
    }

    override fun setTasksCount(count: Int) {
        localRepository.setTasksCount(count)
    }

    override fun getTasksCount(): Int {
        return localRepository.getTasksCount()
    }

    override fun setIsEnableAds(isShown: Boolean) {
        localRepository.setIsEnableAds(isShown)
    }

    override fun isEnableAds(): Boolean {
        return localRepository.isEnableAds()
    }

    override fun setIsShowQuizTask(isShown: Boolean) {
        localRepository.setIsShowQuizTask(isShown)
    }

    override fun isShowQuizTask(): Boolean {
        return localRepository.isShowQuizTask()
    }

    override fun setIsShowSocialMediaTask(isShown: Boolean) {
        localRepository.setIsShowSocialMediaTask(isShown)
    }

    override fun isShowSocialMediaTask(): Boolean {
        return localRepository.isShowSocialMediaTask()
    }

    override fun setIsShowAdTask(isShown: Boolean) {
        localRepository.setIsShowAdTask(isShown)
    }

    override fun isShowAdTask(): Boolean {
        return localRepository.isShowAdTask()
    }

}

