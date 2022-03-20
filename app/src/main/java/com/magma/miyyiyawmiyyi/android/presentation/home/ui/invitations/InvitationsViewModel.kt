package com.magma.miyyiyawmiyyi.android.presentation.home.ui.invitations

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import com.magma.miyyiyawmiyyi.android.data.repository.DataRepository
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class InvitationsViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    override val coroutineContext: CoroutineContext,
) : ViewModel(), CoroutineScope {

    fun getLanguage(): String? {
        return dataRepository.getLang()
    }

    fun getInvitationLink(): String? {
        return dataRepository.getInvitationLink()
    }

    fun setLanguage(lang: String) {
        dataRepository.setLang(lang)
    }

}