package com.magma.miyyiyawmiyyi.android.data.local.repository

import android.content.SharedPreferences
import com.magma.miyyiyawmiyyi.android.data.local.repository.dao.*
import com.magma.miyyiyawmiyyi.android.model.*
import com.magma.miyyiyawmiyyi.android.utils.Const
import javax.inject.Inject

class LocalRepository
@Inject constructor(
    private val taskDao: TaskDao,
    private val giftCardDao: GiftCardDao,
    private val purchaseCardDao: PurchaseCardDao,
    private val ticketDao: TicketDao,
    private val roundDao: RoundDao,
    private val countryDao: CountryDao,
    private val preferences: SharedPreferences
) {

    //Preferences
    fun setGiftCode(giftCode: String) {
        preferences.edit().putString(Const.PREF_GIFT_CODE, giftCode).apply()
    }

    fun getGiftCode(): String? {
        return preferences.getString(Const.PREF_GIFT_CODE, null)
    }
    fun setApiToken(apiToken: String) {
        preferences.edit().putString(Const.PREF_API_TOKEN, apiToken).apply()
    }

    fun getApiToken(): String? {
        return preferences.getString(Const.PREF_API_TOKEN, null)
    }

    fun setRefreshToken(refreshToken: String) {
        preferences.edit().putString(Const.PREF_REFRESH_TOKEN, refreshToken).apply()
    }

    fun getRefreshToken(): String? {
        return preferences.getString(Const.PREF_REFRESH_TOKEN, null)
    }

    fun setLang(lang: String) {
        preferences.edit().putString(Const.PREF_LANG, lang).apply()
    }

    fun getLang(): String? {
        return preferences.getString(Const.PREF_LANG, "ar"/*Locale.getDefault().displayLanguage*/)
    }

    fun setIsShownOnBoarding(isShown: Boolean) {
        preferences.edit().putBoolean(Const.PREF_IS_SHOWN_ONBOARDING, isShown).apply()
    }

    fun isShownOnBoarding(): Boolean {
        return preferences.getBoolean(Const.PREF_IS_SHOWN_ONBOARDING, false)
    }

    fun setIsGeneralNotifications(isGeneral: Boolean) {
        preferences.edit().putBoolean(Const.PREF_IS_GENERAL, isGeneral).apply()
    }

    fun isGeneralNotifications(): Boolean {
        return preferences.getBoolean(Const.PREF_IS_GENERAL, true)
    }

    fun setInvitationLink(link: String) {
        preferences.edit().putString(Const.PREF_INVITATION_LINK, link).apply()
    }

    fun getInvitationLink(): String? {
        return preferences.getString(Const.PREF_INVITATION_LINK, null)
    }

    fun setTasksCount(count: Int) {
        preferences.edit().putInt(Const.PREF_TASKS_COUNT, count).apply()
    }

    fun getTasksCount(): Int {
        return preferences.getInt(Const.PREF_TASKS_COUNT, 0)
    }

    fun setIsEnableAds(isEnable: Boolean) {
        preferences.edit().putBoolean(Const.PREF_IS_ENABLE_ADS, isEnable).apply()
    }

    fun isEnableAds(): Boolean {
        return preferences.getBoolean(Const.PREF_IS_ENABLE_ADS, true)
    }

    fun setIsShowQuizTask(isEnable: Boolean) {
        preferences.edit().putBoolean(Const.PREF_IS_SHOW_QUIZ_TASK, isEnable).apply()
    }

    fun isShowQuizTask(): Boolean {
        return preferences.getBoolean(Const.PREF_IS_SHOW_QUIZ_TASK, true)
    }

    fun setIsShowSocialMediaTask(isEnable: Boolean) {
        preferences.edit().putBoolean(Const.PREF_IS_SHOW_SOCIAL_MEDIA_TASK, isEnable).apply()
    }

    fun isShowSocialMediaTask(): Boolean {
        return preferences.getBoolean(Const.PREF_IS_SHOW_SOCIAL_MEDIA_TASK, true)
    }

    fun setIsShowAdTask(isEnable: Boolean) {
        preferences.edit().putBoolean(Const.PREF_IS_SHOW_AD_TASK, isEnable).apply()
    }

    fun isShowAdTask(): Boolean {
        return preferences.getBoolean(Const.PREF_IS_SHOW_AD_TASK, true)
    }
    //End pref

    //Local DB
    fun loadAllTasks(): List<TaskObj> {
        return taskDao.loadAll()
    }

    fun loadAllTasks(type: String): List<TaskObj> {
        return taskDao.loadAll(type)
    }

    fun loadTask(id: String): TaskObj {
        return taskDao.load(id)
    }

    fun insertTaskList(items: List<TaskObj>): LongArray {
        return taskDao.insertAll(items)
    }

    fun insertTask(item: TaskObj) {
        return taskDao.insert(item)
    }

    fun updateTask(item: TaskObj) {
        return taskDao.update(item)
    }

    fun updateTasks(items: List<TaskObj>) {
        return taskDao.updateAll(items)
    }

    fun deleteTask(item: TaskObj) {
        taskDao.delete(item)
    }

    fun deleteTask(id: String) {
        taskDao.delete(id)
    }

    fun deleteAllTasks() {
        taskDao.deleteAll()
    }

    fun deleteAllTasks(type: String) {
        taskDao.deleteAll(type)
    }

    //Gift Card
    fun loadAllGiftCards(): List<GiftCard> {
        return giftCardDao.loadAll()
    }

    fun loadGiftCard(id: String): GiftCard {
        return giftCardDao.load(id)
    }

    fun insertGiftCardList(items: List<GiftCard>): LongArray {
        return giftCardDao.insertAll(items)
    }

    fun insertGiftCard(item: GiftCard) {
        return giftCardDao.insert(item)
    }

    fun updateGiftCard(item: GiftCard) {
        return giftCardDao.update(item)
    }

    fun updateGiftCards(items: List<GiftCard>) {
        return giftCardDao.updateAll(items)
    }

    fun deleteGiftCard(item: GiftCard) {
        giftCardDao.delete(item)
    }

    fun deleteAllGiftCards() {
        giftCardDao.deleteAll()
    }

    //Purchase Card
    fun loadAllPurchaseCards(): List<PurchaseCard> {
        return purchaseCardDao.loadAll()
    }

    fun loadPurchaseCard(id: String): PurchaseCard {
        return purchaseCardDao.load(id)
    }

    fun insertPurchaseCardList(items: List<PurchaseCard>): LongArray {
        return purchaseCardDao.insertAll(items)
    }

    fun insertPurchaseCard(item: PurchaseCard) {
        return purchaseCardDao.insert(item)
    }

    fun updatePurchaseCard(item: PurchaseCard) {
        return purchaseCardDao.update(item)
    }

    fun updatePurchaseCards(items: List<PurchaseCard>) {
        return purchaseCardDao.updateAll(items)
    }

    fun deletePurchaseCard(item: PurchaseCard) {
        purchaseCardDao.delete(item)
    }

    fun deleteAllPurchaseCards() {
        purchaseCardDao.deleteAll()
    }

    //Ticket
    fun loadAllTickets(): List<Ticket> {
        return ticketDao.loadAll()
    }

    fun loadTicket(id: String): Ticket {
        return ticketDao.load(id)
    }

    fun insertTicket(item: Ticket) {
        ticketDao.insert(item)
    }

    fun insertTicketList(items: List<Ticket>) {
        ticketDao.insertAll(items)
    }

    fun updateTicket(item: Ticket) {
        ticketDao.update(item)
    }

    fun updateTickets(items: List<Ticket>) {
        ticketDao.updateAll(items)
    }

    fun deleteTicket(item: Ticket) {
        ticketDao.delete(item)
    }

    fun deleteAllTickets() {
        ticketDao.deleteAll()
    }

    //Ticket
    fun loadAllRounds(): List<Round> {
        return roundDao.loadAll()
    }

    fun loadRound(id: String): Round {
        return roundDao.load(id)
    }

    fun insertRound(item: Round) {
        roundDao.insert(item)
    }

    fun insertRoundList(items: List<Round>) {
        roundDao.insertAll(items)
    }

    fun updateRound(item: Round) {
        roundDao.update(item)
    }

    fun updateRounds(items: List<Round>) {
        roundDao.updateAll(items)
    }

    fun deleteRound(item: Round) {
        roundDao.delete(item)
    }

    fun deleteAllRounds() {
        roundDao.deleteAll()
    }

    //Country
    fun loadAllCountries(): List<Country> {
        return countryDao.loadAll()
    }

    fun loadCountry(id: String): Country {
        return countryDao.load(id)
    }

    fun insertCountry(item: Country) {
        countryDao.insert(item)
    }

    fun insertCountryList(items: List<Country>) {
        countryDao.insertAll(items)
    }

    fun updateCountry(item: Country) {
        countryDao.update(item)
    }

    fun updateCountries(items: List<Country>) {
        countryDao.updateAll(items)
    }

    fun deleteCountry(item: Country) {
        countryDao.delete(item)
    }

    fun deleteAllCountries() {
        countryDao.deleteAll()
    }

}