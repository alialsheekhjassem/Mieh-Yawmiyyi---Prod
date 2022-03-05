package com.magma.miyyiyawmiyyi.android.presentation.home.ui.quizzes

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import com.magma.miyyiyawmiyyi.android.data.repository.DataRepository
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class QuizzesViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    override val coroutineContext: CoroutineContext,
) : ViewModel(), CoroutineScope {

}