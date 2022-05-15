package com.magma.miyyiyawmiyyi.android.presentation.base

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.magma.miyyiyawmiyyi.android.utils.LocalHelper
import dagger.android.AndroidInjection


open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        LocalHelper.onAttach(this@BaseActivity)

        /*window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )*/
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(LocalHelper.onAttach(base!!))
    }

    override fun onResume() {
        super.onResume()
        LocalHelper.onResume(this@BaseActivity)
    }
}