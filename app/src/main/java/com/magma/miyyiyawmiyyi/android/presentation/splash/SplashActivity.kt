package com.magma.miyyiyawmiyyi.android.presentation.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.magma.miyyiyawmiyyi.android.data.remote.controller.ErrorManager
import com.magma.miyyiyawmiyyi.android.data.remote.controller.Resource
import com.magma.miyyiyawmiyyi.android.data.remote.responses.MyAccountResponse
import dagger.android.AndroidInjection
import com.magma.miyyiyawmiyyi.android.presentation.home.HomeActivity
import com.magma.miyyiyawmiyyi.android.presentation.onboarding.OnBoardingActivity
import com.magma.miyyiyawmiyyi.android.presentation.registration.RegistrationActivity
import com.magma.miyyiyawmiyyi.android.utils.EventObserver
import com.magma.miyyiyawmiyyi.android.utils.LocalHelper
import com.magma.miyyiyawmiyyi.android.utils.ViewModelFactory
import com.magma.miyyiyawmiyyi.android.utils.user_management.ContactManager
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var isShown: Boolean = false
    private var token: String? = null

    private val viewModel: SplashViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[SplashViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        //lang
        LocalHelper.onCreate(this)

        setObservers()

        isShown = viewModel.isShowOnBoarding()
        token = viewModel.getToken()

        if (isShown && token == null)
            goToRegisterActivity()
        else if (isShown && token != null)
            viewModel.getMyAccount()
        else
            goToOnBoardingActivity()

    }

    private fun setObservers() {
        // listen to api result
        viewModel.response.observe(
            this,
            EventObserver
                (object :
                EventObserver.EventUnhandledContent<Resource<MyAccountResponse>> {
                override fun onEventUnhandledContent(t: Resource<MyAccountResponse>) {
                    when (t) {
                        is Resource.Loading -> {
                            // show progress bar and remove no data layout while loading
                            //binding.progress.visibility = View.VISIBLE
                            //binding.txtEmpty.visibility = View.GONE
                        }
                        is Resource.Success -> {
                            // response is ok get the data and display it in the list
                            //binding.progress.visibility = View.GONE
                            val response = t.response as MyAccountResponse
                            Log.d("TAG", "response: $response")
                            onFetchedAccountSuccess(response)
                        }
                        is Resource.DataError -> {
                            //binding.progress.visibility = View.GONE
                            // usually this happening when there is server error
                            val response = t.response as ErrorManager
                            Log.d("TAG", "response: DataError $response")
                            //showErrorToast(response.failureMessage)
                            Toast.makeText(applicationContext, response.failureMessage,
                                Toast.LENGTH_LONG).show()
                            onFailure()
                        }
                        is Resource.Exception -> {
                            //binding.progress.visibility = View.GONE
                            // usually this happening when there is no internet
                            val response = t.response
                            Log.d("TAG", "response: $response")
                            //showErrorToast(response.toString())
                            Toast.makeText(applicationContext, response.toString(),
                                Toast.LENGTH_LONG).show()
                            onFailure()
                        }
                    }
                }
            })
        )
    }

    private fun onFailure() {
        if (isShown)
            goToRegisterActivity()
        else
            goToOnBoardingActivity()
    }

    private fun onFetchedAccountSuccess(response: MyAccountResponse) {
        ContactManager.setAccount(response)
        if (token != null)
            goToHomeActivity()
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