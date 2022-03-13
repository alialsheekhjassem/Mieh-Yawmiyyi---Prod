package com.magma.miyyiyawmiyyi.android.data.local.repository

import android.content.SharedPreferences
import com.magma.miyyiyawmiyyi.android.data.local.repository.dao.GiftCardDao
import com.magma.miyyiyawmiyyi.android.data.local.repository.dao.PurchaseCardDao
import com.magma.miyyiyawmiyyi.android.data.local.repository.dao.TaskDao
import com.magma.miyyiyawmiyyi.android.model.GiftCard
import com.magma.miyyiyawmiyyi.android.model.PurchaseCard
import com.magma.miyyiyawmiyyi.android.model.TaskObj
import com.magma.miyyiyawmiyyi.android.utils.Const
import java.util.*
import javax.inject.Inject

class LocalRepository
@Inject constructor(
    private val taskDao: TaskDao,
    private val giftCardDao: GiftCardDao,
    private val purchaseCardDao: PurchaseCardDao,
    private val preferences: SharedPreferences
) {

    //Preferences
    fun setApiToken(apiToken: String) {
        preferences.edit().putString(Const.PREF_API_TOKEN, apiToken).apply()
    }

    fun getApiToken(): String? {
        return preferences.getString(Const.PREF_API_TOKEN, null)
    }

    fun setLang(lang: String) {
        preferences.edit().putString(Const.PREF_LANG, lang).apply()
    }

    fun getLang(): String? {
        return preferences.getString(Const.PREF_LANG, Locale.getDefault().displayLanguage)
    }

    fun setIsShownOnBoarding(isShown: Boolean) {
        preferences.edit().putBoolean(Const.PREF_IS_SHOWN_ONBOARDING, isShown).apply()
    }

    fun isShownOnBoarding(): Boolean {
        return preferences.getBoolean(Const.PREF_IS_SHOWN_ONBOARDING, false)
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

    fun deleteAllTasks() {
        taskDao.deleteAll()
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

}