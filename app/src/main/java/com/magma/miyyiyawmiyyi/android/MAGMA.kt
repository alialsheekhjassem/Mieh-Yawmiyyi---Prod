package com.magma.miyyiyawmiyyi.android

import android.app.Application
import com.facebook.ads.AdSettings
import com.facebook.ads.AudienceNetworkAds
import com.magma.miyyiyawmiyyi.android.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

open class MAGMA : Application(), HasAndroidInjector {
    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder().application(this).build().inject(this)

        AudienceNetworkAds.initialize(this)
        //This will make the ad run on the test device, let's say your Android AVD emulator
        AdSettings.setTestMode(true)
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

}