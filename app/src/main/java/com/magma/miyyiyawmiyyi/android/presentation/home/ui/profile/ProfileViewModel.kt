package com.magma.miyyiyawmiyyi.android.presentation.home.ui.profile

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import com.magma.miyyiyawmiyyi.android.data.repository.DataRepository
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ProfileViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    override val coroutineContext: CoroutineContext,
) : ViewModel(), CoroutineScope {

}