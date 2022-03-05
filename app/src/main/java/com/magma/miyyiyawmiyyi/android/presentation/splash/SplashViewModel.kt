package com.magma.miyyiyawmiyyi.android.presentation.splash

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import com.magma.miyyiyawmiyyi.android.data.repository.DataRepository
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class SplashViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    override val coroutineContext: CoroutineContext,
) : ViewModel(), CoroutineScope {

    fun isShowOnBoarding(): Boolean {
        return dataRepository.isShownOnBoarding()
    }

    fun getToken(): String? {
        return dataRepository.getApiToken()
    }

}