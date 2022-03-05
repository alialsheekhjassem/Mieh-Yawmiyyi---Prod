package com.magma.miyyiyawmiyyi.android.presentation.onboarding

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import com.magma.miyyiyawmiyyi.android.data.repository.DataRepository
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class OnBoardingViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    override val coroutineContext: CoroutineContext,
) : ViewModel(), CoroutineScope {

    fun setIsShowOnBoarding(isShown: Boolean) {
        dataRepository.setIsShownOnBoarding(isShown)
    }

}