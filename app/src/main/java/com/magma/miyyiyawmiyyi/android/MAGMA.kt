package com.magma.miyyiyawmiyyi.android

import android.app.Application
import android.util.Log
import com.facebook.ads.AdSettings
import com.facebook.ads.AudienceNetworkAds
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.magma.miyyiyawmiyyi.android.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

open class MAGMA : Application(), HasAndroidInjector {
    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    var rewardedAd: RewardedAd? = null
    var isLoading = false

    val TAG = "MAGMA"

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder().application(this).build().inject(this)
        mApp = this

        AudienceNetworkAds.initialize(this)
        //This will make the ad run on the test device, let's say your Android AVD emulator
        AdSettings.setTestMode(true)
        AdSettings.setIntegrationErrorMode(AdSettings.IntegrationErrorMode.INTEGRATION_ERROR_CALLBACK_MODE)
        loadRewardedAd()
    }

    companion object {
        lateinit var mApp : MAGMA

        fun getInstance(): MAGMA {
            return mApp
        }
    }

    /**
     * load Rewarded ad
     */
    fun loadRewardedAd() {
        if (rewardedAd == null) {
            val reworded = intArrayOf(
                R.string.admob_rewarded_ad_id,
                R.string.admob_rewarded_ad_id10
            )
            isLoading = true
            val adRequest = AdRequest.Builder().build()
            RewardedAd.load(
                this@MAGMA,
                getString(reworded[(Math.random() * 2).toInt()]),
                adRequest,
                object : RewardedAdLoadCallback() {
                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        // Handle the error.
                        Log.d(
                            "$TAG ERROR",
                            loadAdError.message
                        )
                        rewardedAd = null
                        isLoading = false
                    }

                    override fun onAdLoaded(rewardedAd2: RewardedAd) {
                        rewardedAd = rewardedAd2
                        Log.d(TAG, "onAdLoaded")
                        isLoading = false
                    }
                })
        }
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

}