package com.magma.miyyiyawmiyyi.android.data.repository

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

interface DataSource {

    //Api
    suspend fun doServerLogin(loginRequest: LoginRequest): Resource<LoginResponse>
    suspend fun doServerRegister(registerRequest: RegisterRequest): Resource<ResponseWrapper<String>>
    suspend fun doServerResetPassword(request: ResetPasswordRequest): Resource<ResponseWrapper<String>>
    suspend fun getTasks(limit: Int, offset: Int): Resource<TasksResponse>
    suspend fun getGifts(limit: Int, offset: Int): Resource<GiftStoreResponse>
    suspend fun getPurchases(limit: Int, offset: Int): Resource<GiftStorePurchasesResponse>
    suspend fun getRounds(limit: Int, offset: Int, status: String?, id: String?): Resource<RoundsResponse>
    suspend fun getMyAccount(): Resource<MyAccountResponse>
    suspend fun doServerUpdateMyAccount(accountRequest: AccountRequest): Resource<MyAccountResponse>

    //Local
    fun loadAllTasks(): List<TaskObj>
    fun loadAllTasks(type: String): List<TaskObj>
    fun loadTask(id: String): TaskObj
    fun insertTaskList(items: List<TaskObj>)
    fun insertTask(item: TaskObj)
    fun updateTask(item: TaskObj)
    fun updateTasks(items: List<TaskObj>)
    fun deleteTask(item: TaskObj)
    fun deleteAllTasks()
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

    //Pref
    fun setApiToken(apiToken: String)
    fun getApiToken(): String?
    fun setLang(lang: String)
    fun getLang(): String?
    fun setIsShownOnBoarding(isShown: Boolean)
    fun isShownOnBoarding(): Boolean?
}