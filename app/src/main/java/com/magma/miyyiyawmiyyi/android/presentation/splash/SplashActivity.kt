package com.magma.miyyiyawmiyyi.android.presentation.splash

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.magma.miyyiyawmiyyi.android.R
import com.magma.miyyiyawmiyyi.android.BuildConfig
import com.magma.miyyiyawmiyyi.android.data.remote.controller.ErrorManager
import com.magma.miyyiyawmiyyi.android.data.remote.controller.Resource
import com.magma.miyyiyawmiyyi.android.data.remote.responses.InfoResponse
import com.magma.miyyiyawmiyyi.android.data.remote.responses.MyAccountResponse
import com.magma.miyyiyawmiyyi.android.data.remote.responses.RoundsResponse
import com.magma.miyyiyawmiyyi.android.databinding.DialogUpdateAvailableBinding
import com.magma.miyyiyawmiyyi.android.model.Setting
import com.magma.miyyiyawmiyyi.android.presentation.base.BaseActivity
import dagger.android.AndroidInjection
import com.magma.miyyiyawmiyyi.android.presentation.home.HomeActivity
import com.magma.miyyiyawmiyyi.android.presentation.onboarding.OnBoardingActivity
import com.magma.miyyiyawmiyyi.android.presentation.registration.RegistrationActivity
import com.magma.miyyiyawmiyyi.android.utils.EventObserver
import com.magma.miyyiyawmiyyi.android.utils.ViewModelFactory
import com.magma.miyyiyawmiyyi.android.utils.user_management.ContactManager
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var isShown: Boolean = false
    private var token: String? = null

    private val TAG = "SplashActivity"

    private val viewModel: SplashViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[SplashViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        setObservers()

        isShown = viewModel.isShowOnBoarding()
        token = viewModel.getToken()

        if (isShown && token.isNullOrEmpty())
            goToRegisterActivity()
        else if (isShown && !token.isNullOrEmpty()) {
            viewModel.getInfo()
            //viewModel.getRounds(limit = 20, offset = 0, Const.STATUS_ACTIVE, null)
        } else
            goToOnBoardingActivity()

    }

    private fun setObservers() {
        // listen to api result
        viewModel.infoResponse.observe(
            this,
            EventObserver
                (object :
                EventObserver.EventUnhandledContent<Resource<InfoResponse>> {
                override fun onEventUnhandledContent(t: Resource<InfoResponse>) {
                    when (t) {
                        is Resource.Loading -> {
                        }
                        is Resource.Success -> {
                            // response is ok get the data and display it in the list
                            val response = t.response as InfoResponse
                            Log.d(TAG, "response: Success $response")
                            ContactManager.setInfo(response)
                            viewModel.getMyAccount()
                            response.settings?.let { setSettings(it) }
                        }
                        is Resource.DataError -> {
                            // usually this happening when there is server error
                            val response = t.response as ErrorManager
                            Log.d(TAG, "response: DataError $response")
                        }
                        is Resource.Exception -> {
                            // usually this happening when there is no internet
                            val response = t.response
                            Log.d(TAG, "response: Exception $response")
                        }
                    }
                }
            })
        )
        // listen to api result
        viewModel.roundResponse.observe(
            this,
            EventObserver
                (object :
                EventObserver.EventUnhandledContent<Resource<RoundsResponse>> {
                override fun onEventUnhandledContent(t: Resource<RoundsResponse>) {
                    when (t) {
                        is Resource.Loading -> {
                            // show progress bar and remove no data layout while loading
                            //binding.progress.visibility = View.VISIBLE
                            //binding.txtEmpty.visibility = View.GONE
                        }
                        is Resource.Success -> {
                            // response is ok get the data and display it in the list
                            //binding.progress.visibility = View.GONE
                            val response = t.response as RoundsResponse
                            Log.d(TAG, "response: Success $response")

                            viewModel.deleteAndSaveRounds(response.items)
                        }
                        is Resource.DataError -> {
                            //binding.progress.visibility = View.GONE
                            // usually this happening when there is server error
                            val response = t.response as ErrorManager
                            Log.d(TAG, "response: DataError $response")
                        }
                        is Resource.Exception -> {
                            //binding.progress.visibility = View.GONE
                            // usually this happening when there is no internet
                            val response = t.response
                            Log.d(TAG, "response: Exception $response")
                        }
                    }
                }
            })
        )
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
                            Log.d("TAG", "response: Success $response")
                            onFetchedAccountSuccess(response)
                        }
                        is Resource.DataError -> {
                            //binding.progress.visibility = View.GONE
                            // usually this happening when there is server error
                            val response = t.response as ErrorManager
                            Log.d("TAG", "response: DataError $response")
                            //showErrorToast(response.failureMessage)
                            Toast.makeText(
                                applicationContext, response.failureMessage,
                                Toast.LENGTH_LONG
                            ).show()
                            onFailure()
                        }
                        is Resource.Exception -> {
                            //binding.progress.visibility = View.GONE
                            // usually this happening when there is no internet
                            val response = t.response
                            Log.d("TAG", "response: Exception $response")
                            //showErrorToast(response.toString())
                            Toast.makeText(
                                applicationContext, response.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                            onFailure()
                        }
                    }
                }
            })
        )
    }

    private fun setSettings(t: List<Setting>) {

        Log.d(TAG, "QQQ onEventUnhandledContent: $t")

        val itemAds =
            t.find { setting -> setting.name?.contains("ads_enable") ?: true }
        val itemLink =
            t.find { setting ->
                setting.name?.contains("google_play_share_link") ?: true
            }
        val itemIsShowQuizTask =
            t.find { setting -> setting.name?.contains("android_show_quiz") ?: true }
        val itemIsShowSocialMediaTask =
            t.find { setting -> setting.name?.contains("android_show_social_media_tasks") ?: true }
        val itemIsShowAdTask =
            t.find { setting -> setting.name?.contains("android_show_ads_tasks") ?: true }

        /*val itemWeb =
            t.find { setting -> setting.name?.contains("web_share_link") ?: true }*/
        val itemMin =
            t.find { setting -> setting.name?.contains("android_min_ver") ?: true }
        val itemMax =
            t.find { setting -> setting.name?.contains("android_max_ver") ?: true }

        itemAds?.value?.let {
            Log.d(TAG, "QQQ itemAds onEventUnhandledContent: $it")
            viewModel.setIsEnableAds(it.toBoolean())
        }
        itemIsShowQuizTask?.value?.let {
            Log.d(TAG, "QQQ itemIsShowQuizTask onEventUnhandledContent: $it")
            viewModel.setIsShowQuizTask(it.toBoolean())
        }
        itemIsShowSocialMediaTask?.value?.let {
            Log.d(TAG, "QQQ itemIsShowSocialMediaTask onEventUnhandledContent: $it")
            viewModel.setIsShowSocialMediaTask(it.toBoolean())
        }
        itemIsShowAdTask?.value?.let {
            Log.d(TAG, "QQQ itemIsShowAdTask onEventUnhandledContent: $it")
            viewModel.setIsShowAdTask(it.toBoolean())
        }

        val currentVersion = BuildConfig.VERSION_CODE
        itemMin?.let { setting ->
            setting.value?.let {
                if (currentVersion < it.toInt()) {
                    showUpdateDialog(itemLink, false)
                } else itemMax?.value?.let { max ->
                    if (currentVersion < max.toInt()) {
                        showUpdateDialog(itemLink, true)
                    }
                }
            }
        }
    }

    private fun showUpdateDialog(itemLink: Setting?, cancelAble: Boolean) {
        val builder = AlertDialog.Builder(this)
        val dialogBinding: DialogUpdateAvailableBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this), R.layout.dialog_update_available, null, false
        )
        builder.setView(dialogBinding.root)
        val alertDialog = builder.create()
        if (!cancelAble) {
            dialogBinding.txtTitle.text = getString(R.string.please_update_app)
            dialogBinding.txtBody.text = getString(R.string.update_available_body_force)
            dialogBinding.btnLater.visibility = View.GONE
        }
        dialogBinding.btnUpdate.setOnClickListener {
            if (cancelAble)
                alertDialog.dismiss()
            itemLink?.value?.let {
                openGooglePlayLink(it)
            }
        }
        alertDialog.setCancelable(cancelAble)
        if (cancelAble)
            dialogBinding.btnLater.setOnClickListener { alertDialog.dismiss() }
        alertDialog.show()
    }

    private fun openGooglePlayLink(link: String) {
        val intentInvite = Intent(Intent.ACTION_SEND)
        intentInvite.type = "text/plain"
        val subject = resources.getString(R.string.about_us)
        intentInvite.putExtra(Intent.EXTRA_SUBJECT, subject)
        intentInvite.putExtra(Intent.EXTRA_TEXT, link)
        startActivity(Intent.createChooser(intentInvite, "Share using"))
    }

    private fun onFailure() {
        if (isShown)
            goToRegisterActivity()
        else
            goToOnBoardingActivity()
    }

    private fun onFetchedAccountSuccess(response: MyAccountResponse) {
        ContactManager.setAccount(response)
        if (!token.isNullOrEmpty()) {
            if (response.account?.name.isNullOrEmpty())
                goToRegisterActivity()
            else
                goToHomeActivity()
        }
    }

    private fun goToOnBoardingActivity() {
        val intent = Intent(this@SplashActivity, OnBoardingActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goToHomeActivity() {
        val mIntent = Intent(this@SplashActivity, HomeActivity::class.java)
        startActivity(mIntent)
        finish()
    }

    private fun goToRegisterActivity() {
        val intent = Intent(this@SplashActivity, RegistrationActivity::class.java)
        startActivity(intent)
        finish()
    }

}