package com.magma.miyyiyawmiyyi.android.data.repository

import com.magma.miyyiyawmiyyi.android.data.local.repository.LocalRepository
import com.magma.miyyiyawmiyyi.android.data.remote.controller.Resource
import com.magma.miyyiyawmiyyi.android.data.remote.controller.ResponseWrapper
import com.magma.miyyiyawmiyyi.android.data.remote.requests.AccountRequest
import com.magma.miyyiyawmiyyi.android.data.remote.requests.LoginRequest
import com.magma.miyyiyawmiyyi.android.data.remote.requests.RegisterRequest
import com.magma.miyyiyawmiyyi.android.data.remote.requests.ResetPasswordRequest
import com.magma.miyyiyawmiyyi.android.data.remote.responses.*
import com.magma.miyyiyawmiyyi.android.model.GiftCard
import com.magma.miyyiyawmiyyi.android.model.PurchaseCard
import com.magma.miyyiyawmiyyi.android.model.TaskObj
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

    override suspend fun doServerRegister(registerRequest: RegisterRequest): Resource<ResponseWrapper<String>> {
        return remoteRepository.doServerRegister(registerRequest)
    }

    override suspend fun doServerResetPassword(request: ResetPasswordRequest): Resource<ResponseWrapper<String>> {
        return remoteRepository.doServerResetPassword(request)
    }

    override suspend fun getTasks(limit: Int, offset: Int): Resource<TasksResponse> {
        return remoteRepository.getTasks(limit, offset)
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

    override suspend fun getRounds(limit: Int, offset: Int, status: String?, id: String?): Resource<RoundsResponse> {
        return remoteRepository.getRounds(limit, offset, status, id)
    }

    override suspend fun getMyAccount(): Resource<MyAccountResponse> {
        return remoteRepository.getMyAccount()
    }

    override suspend fun doServerUpdateMyAccount(accountRequest: AccountRequest): Resource<MyAccountResponse> {
        return remoteRepository.doServerUpdateMyAccount(accountRequest)
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

    override fun deleteAllTasks() {
        localRepository.deleteAllTasks()
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


    //Pref
    override fun setApiToken(apiToken: String) {
        localRepository.setApiToken(apiToken)
    }

    override fun getApiToken(): String? {
        return localRepository.getApiToken()
    }

    override fun setLang(lang: String) {
        localRepository.setLang(lang)
    }

    override fun getLang(): String? {
        return localRepository.getLang()
    }

    override fun setIsShownOnBoarding(isShown: Boolean) {
        return localRepository.setIsShownOnBoarding(isShown)
    }

    override fun isShownOnBoarding(): Boolean {
        return localRepository.isShownOnBoarding()
    }

}

