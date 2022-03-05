package com.magma.miyyiyawmiyyi.android.presentation.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import dagger.android.AndroidInjection
import com.magma.miyyiyawmiyyi.android.presentation.home.HomeActivity
import com.magma.miyyiyawmiyyi.android.presentation.onboarding.OnBoardingActivity
import com.magma.miyyiyawmiyyi.android.presentation.registration.RegistrationActivity
import com.magma.miyyiyawmiyyi.android.utils.LocalHelper
import com.magma.miyyiyawmiyyi.android.utils.ViewModelFactory
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: SplashViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[SplashViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        //lang
        LocalHelper.onCreate(this)

        val isShown = viewModel.isShowOnBoarding()
        val token = viewModel.getToken()
        if (isShown && token == null)
            goToRegisterActivity()
        else if (isShown && token != null)
            goToHomeActivity()
        else
            goToOnBoardingActivity()

    }

    private fun goToOnBoardingActivity() {
        val intent = Intent(this@SplashActivity, OnBoardingActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goToHomeActivity() {
        val intent = Intent(this@SplashActivity, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goToRegisterActivity() {
        val intent = Intent(this@SplashActivity, RegistrationActivity::class.java)
        startActivity(intent)
        finish()
    }

}