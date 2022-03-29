package com.magma.miyyiyawmiyyi.android.presentation.home

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.get
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.facebook.ads.*
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import dagger.android.AndroidInjection
import com.magma.miyyiyawmiyyi.android.R
import com.magma.miyyiyawmiyyi.android.data.remote.controller.ErrorManager
import com.magma.miyyiyawmiyyi.android.data.remote.controller.Resource
import com.magma.miyyiyawmiyyi.android.data.remote.responses.InfoResponse
import com.magma.miyyiyawmiyyi.android.data.remote.responses.MyAccountResponse
import com.magma.miyyiyawmiyyi.android.data.remote.responses.RoundsResponse
import com.magma.miyyiyawmiyyi.android.databinding.ActivityHomeBinding
import com.magma.miyyiyawmiyyi.android.presentation.registration.RegistrationActivity
import com.magma.miyyiyawmiyyi.android.presentation.splash.SplashViewModel
import com.magma.miyyiyawmiyyi.android.utils.CommonUtils.showLoadingDialog
import com.magma.miyyiyawmiyyi.android.utils.Const
import com.magma.miyyiyawmiyyi.android.utils.EventObserver
import com.magma.miyyiyawmiyyi.android.utils.LocalHelper
import com.magma.miyyiyawmiyyi.android.utils.ViewModelFactory
import com.magma.miyyiyawmiyyi.android.utils.user_management.ContactManager
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.app_bar_main.view.*
import java.util.*
import javax.inject.Inject

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var mDrawerToggle: ActionBarDrawerToggle
    var mToolBarNavigationListenerIsRegistered = false

    private lateinit var alertDialog: AlertDialog

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    var mInterstitialAd: InterstitialAd? = null
    var canShowFullscreenAd = false

    private var mCountDownTimer: CountDownTimer? = null
    private var mGameIsInProgress = false
    private var mAdIsLoading: Boolean = false
    private var mTimerMilliseconds = 0L

    private val TAG = "HomeActivity"

    private val viewModel: SplashViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[SplashViewModel::class.java]
    }

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        //lang
        LocalHelper.onCreate(this)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.appBar.toolbar
        val drawerLayout = binding.drawerLayout
        setSupportActionBar(toolbar)

        // Instantiate an InterstitialAd object.
        // NOTE: the placement ID will eventually identify this as your App, you can ignore it for
        // now, while you are testing and replace it later when you have signed up.
        // While you are using this temporary code you will only get test ads and if you release
        // your code like this to the Google Play your users will not receive ads (you will get a no fill error).
        mInterstitialAd = InterstitialAd(this, Const.FB_INTERSTITIAL)

        mDrawerToggle = object : ActionBarDrawerToggle(
            this, drawerLayout,
            toolbar, R.string.navigation_drawer_close, R.string.navigation_drawer_close
        ) {
        }
        drawerLayout.addDrawerListener(mDrawerToggle)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_tasks, R.id.navigation_tickets,
                R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        val bottomNavView: BottomNavigationView = binding.appBar.content.bottomNavView
        bottomNavView.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        setupActionBarWithNavController(navController)
        NavigationUI.setupWithNavController(binding.navView, navController)

        navController.addOnDestinationChangedListener { nc: NavController, nd: NavDestination, args: Bundle? ->
            toolbar.toolbar_title.text = nd.label
            //enableDrawer(true)
            toolbar.setNavigationIcon(R.drawable.ic_feather_menu)
            toolbar.navigationIcon?.setTint(ActivityCompat.getColor(this, R.color.colorPrimary))

            if (nd.id == R.id.navigation_home || nd.id == R.id.navigation_live_stream ||
                /*nd.id == R.id.navigation_profile || nd.id == R.id.navigation_tasks ||*/
                nd.id == R.id.navigation_tickets
            ) {
                bottomNavView.visibility = View.VISIBLE
                binding.appBar.coordinatorLayout.setBackgroundResource(0)
                toolbar.setBackgroundResource(R.drawable.app_bar_bkg)
                toolbar.elevation = 5F
            } else {
                if (nd.id != R.id.navigation_profile && nd.id != R.id.navigation_tasks) {
                    toolbar.setNavigationIcon(R.drawable.ic_circle_filled_left)
                    bottomNavView.visibility = View.GONE
                } else {
                    bottomNavView.visibility = View.VISIBLE
                }
                toolbar.setBackgroundResource(0)
                binding.appBar.coordinatorLayout.setBackgroundResource(R.drawable.bg_grey)
                toolbar.elevation = 0F
            }

            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            if (screensWithHumbIcon(nd, nc)) {
                enableDrawer(true)
                toolbar.setNavigationIcon(R.drawable.ic_feather_menu)
            } else {
                enableDrawer(false)
                toolbar.setNavigationIcon(R.drawable.ic_circle_filled_left)
            }
        }
        mDrawerToggle.syncState()
        toolbar.setNavigationIcon(R.drawable.ic_feather_menu)

        setObservers()

        if (ContactManager.getCurrentAccount()?.account == null) {
            viewModel.getMyAccount()
            viewModel.getInfo()
        }

        binding.txtLogout.setOnClickListener {
            viewModel.doServerLogout()
        }

        //subscribe topics
        subscribeTopic(Const.TOPIC_ROUNDS)
        subscribeTopic(Const.TOPIC_GRAND_PRIZE)
        if (LocalHelper.locale?.language == "ar") {
            unSubscribeTopic(Const.TOPIC_GENERAL_EN)
            subscribeTopic(Const.TOPIC_GENERAL_AR)
        } else {
            unSubscribeTopic(Const.TOPIC_GENERAL_AR)
            subscribeTopic(Const.TOPIC_GENERAL_EN)
        }
    }

    private fun screensWithHumbIcon(
        nd: NavDestination,
        nc: NavController,
    ) =
        nd.id == nc.graph[R.id.navigation_home].id ||
                nd.id == nc.graph[R.id.navigation_tasks].id ||
                nd.id == nc.graph[R.id.navigation_tickets].id ||
                nd.id == nc.graph[R.id.navigation_live_stream].id ||
                nd.id == nc.graph[R.id.navigation_profile].id

    private fun enableDrawer(b: Boolean) {
        if (!b) {
            // Remove hamburger
            mDrawerToggle.isDrawerIndicatorEnabled = false
            // Show back button
            supportActionBar?.setHomeButtonEnabled(true)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            toolbar.setNavigationIcon(R.drawable.ic_circle_filled_left)
            // when DrawerToggle is disabled i.e. setDrawerIndicatorEnabled(false), navigation icon
            // clicks are disabled i.e. the UP button will not work.
            // We need to add a listener, as in below, so DrawerToggle will forward
            // click events to this listener.
            if (!mToolBarNavigationListenerIsRegistered) {
                mDrawerToggle.toolbarNavigationClickListener =
                    View.OnClickListener { onBackPressed() }
                mToolBarNavigationListenerIsRegistered = true
            }
        } else {
            //You must regain the power of swipe for the drawer.
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

            // Remove back button
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_feather_menu)
            mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_feather_menu)
            supportActionBar!!.setDisplayHomeAsUpEnabled(false)
            // Show hamburger
            mDrawerToggle.isDrawerIndicatorEnabled = true
            toolbar.setNavigationIcon(R.drawable.ic_feather_menu)
            // Remove the/any drawer toggle listener
            mDrawerToggle.toolbarNavigationClickListener = null
            mToolBarNavigationListenerIsRegistered = false
            toolbar.setNavigationIcon(R.drawable.ic_feather_menu)

        }
        mDrawerToggle.syncState()

    }

    private fun setObservers() {
        // listen to api result
        viewModel.logoutResponse.observe(
            this,
            EventObserver
                (object :
                EventObserver.EventUnhandledContent<Resource<Any?>> {
                override fun onEventUnhandledContent(t: Resource<Any?>) {
                    when (t) {
                        is Resource.Loading -> {
                            showLoadingDialog()
                        }
                        is Resource.Success -> {
                            // response is ok get the data and display it in the list
                            val response = t.response
                            Log.d(TAG, "response: $response")
                            ContactManager.refreshInstance()
                            hideLoadingDialog()
                            goToRegistration()
                        }
                        is Resource.DataError -> {
                            // usually this happening when there is server error
                            val response = t.response as ErrorManager
                            Log.d(TAG, "response: DataError $response")
                            hideLoadingDialog()
                            showToast(response.failureMessage)
                        }
                        is Resource.Exception -> {
                            // usually this happening when there is no internet
                            val response = t.response
                            Log.d(TAG, "response: $response")
                            hideLoadingDialog()
                            showToast(response.toString())
                        }
                    }
                }
            })
        )
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
                            Log.d(TAG, "response: $response")
                            ContactManager.setInfo(response)
                        }
                        is Resource.DataError -> {
                            // usually this happening when there is server error
                            val response = t.response as ErrorManager
                            Log.d(TAG, "response: DataError $response")
                        }
                        is Resource.Exception -> {
                            // usually this happening when there is no internet
                            val response = t.response
                            Log.d(TAG, "response: $response")
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
                            Log.d(TAG, "response: $response")

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
                            Log.d(TAG, "response: $response")
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
                            Log.d("TAG", "response: $response")
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
                            Log.d("TAG", "response: $response")
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

    private fun goToRegistration() {
        val intent = Intent(baseContext, RegistrationActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun onFailure() {

    }

    private fun onFetchedAccountSuccess(response: MyAccountResponse) {
        ContactManager.setAccount(response)
    }

    fun showLoadingDialog() {
        alertDialog = showLoadingDialog(this)
    }

    fun hideLoadingDialog() {
        alertDialog.cancel()
    }

    fun showToast(success: String) {
        Toast.makeText(this, success, Toast.LENGTH_LONG).show()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LocalHelper.setLocale(this, Locale.getDefault().language)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(this, R.id.nav_host_fragment)
        return (navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp())
    }

    private fun subscribeTopic(topic: String) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
            .addOnCompleteListener { task: Task<Void> ->
                if (task.isSuccessful) {
                    Log.d(TAG, "subscribeTopics: Success $topic")
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "subscribeTopics: Failure $it")
            }
    }

    private fun unSubscribeTopic(topic: String) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
            .addOnCompleteListener { task: Task<Void> ->
                if (task.isSuccessful) {
                    Log.d(TAG, "unSubscribeTopic: Success $topic")
                }
            }
    }


    // Create the game timer, which counts down to the end of the level
    // and shows the "retry" button.
    private fun createTimer(milliseconds: Long) {
        mCountDownTimer?.cancel()

        mCountDownTimer = object : CountDownTimer(milliseconds, 50) {
            override fun onTick(millisUntilFinished: Long) {
                mTimerMilliseconds = millisUntilFinished
                //timer.text = "seconds remaining: ${ millisUntilFinished / 1000 + 1 }"
            }

            override fun onFinish() {
                mGameIsInProgress = false
                //timer.text = "done!"
            }
        }
    }

    // Request a new ad if one isn't already loaded, hide the button, and kick off the timer.
    fun startGame() {
        /*if (!mAdIsLoading && mInterstitialAd == null) {
            mAdIsLoading = true
            loadAd()
        }
        resumeGame(Const.GAME_LENGTH_MILLISECONDS)*/

        loadAd()
        /*if (!mAdIsLoading && mInterstitialAd == null){
            mAdIsLoading = true
            loadAd()
        }*/
    }

    override fun onPause() {
        super.onPause()
        canShowFullscreenAd = false
    }

    override fun onResume() {
        super.onResume()
        canShowFullscreenAd = true
    }

    private fun loadAd() {
        AudienceNetworkAds.initialize(this)
        mInterstitialAd = InterstitialAd(this, Const.FB_INTERSTITIAL)

        // Create listeners for the Interstitial Ad
        val interstitialAdListener: InterstitialAdListener = object : InterstitialAdListener {
            override fun onInterstitialDisplayed(ad: Ad) {
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.")
            }

            override fun onInterstitialDismissed(ad: Ad) {
                // Interstitial dismissed callback
                Log.e(TAG, "Interstitial ad dismissed.")
            }

            override fun onError(ad: Ad?, adError: AdError) {
                // Ad error callback
                Log.e(TAG, "Interstitial ad failed to load: " + adError.errorMessage)
                Toast.makeText(
                    this@HomeActivity,
                    "Error loading ad: " + adError.errorMessage, Toast.LENGTH_SHORT
                ).show()
            }

            override fun onAdLoaded(ad: Ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!")
                if (canShowFullscreenAd) {
                    mInterstitialAd?.show()
                }
            }

            override fun onAdClicked(ad: Ad) {
                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!")
            }

            override fun onLoggingImpression(ad: Ad) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!")
            }
        }

        val interstitialLoadAdConfig =
            mInterstitialAd?.buildLoadAdConfig()
                ?.withAdListener(interstitialAdListener)
                ?.build()
        mInterstitialAd?.loadAd(interstitialLoadAdConfig)

        showAdWithDelay()
    }

    private fun showAdWithDelay() {
        /**
         * Here is an example for displaying the ad with delay;
         * Please do not copy the Handler into your project
         */
        Looper.myLooper()?.let {
            Handler(it).postDelayed({ // Check if interstitialAd has been loaded successfully
                if (mInterstitialAd == null || mInterstitialAd?.isAdLoaded == false) {
                    return@postDelayed
                }
                // Check if ad is already expired or invalidated, and do not show ad if that is the case. You will not get paid to show an invalidated ad.
                if (mInterstitialAd?.isAdInvalidated == true) {
                    return@postDelayed
                }
                // Show the ad
                mInterstitialAd?.show()
            }, 1000 * 60 * 15)
        }
    } // Show the ad after 15 minutes
}