package com.magma.miyyiyawmiyyi.android.presentation.home.ui.settings

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import com.magma.miyyiyawmiyyi.android.data.repository.DataRepository
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class SettingsViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    override val coroutineContext: CoroutineContext,
) : ViewModel(), CoroutineScope {
    fun getLanguage(): String? {
        return dataRepository.getLang()
    }

    fun setLanguage(lang: String) {
        dataRepository.setLang(lang)
    }

    fun isGeneralNotifications(): Boolean {
        return dataRepository.isGeneralNotifications()
    }

    fun setIsGeneralNotifications(isGeneral: Boolean) {
        dataRepository.setIsGeneralNotifications(isGeneral)
    }

}