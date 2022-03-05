package com.magma.miyyiyawmiyyi.android.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import com.magma.miyyiyawmiyyi.android.presentation.details.DetailsActivity
import com.magma.miyyiyawmiyyi.android.presentation.edit_profile.EditProfileActivity
import com.magma.miyyiyawmiyyi.android.presentation.home.HomeActivity
import com.magma.miyyiyawmiyyi.android.presentation.onboarding.OnBoardingActivity
import com.magma.miyyiyawmiyyi.android.presentation.registration.RegistrationActivity
import com.magma.miyyiyawmiyyi.android.presentation.splash.SplashActivity

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeSplashActivity(): SplashActivity

    @ContributesAndroidInjector
    abstract fun contributeOnBoardingActivity(): OnBoardingActivity

    @ContributesAndroidInjector
    abstract fun contributeRegistrationActivity(): RegistrationActivity

    @ContributesAndroidInjector
    abstract fun contributeHomeActivity(): HomeActivity

    @ContributesAndroidInjector
    abstract fun contributeDetailsActivity(): DetailsActivity

    @ContributesAndroidInjector
    abstract fun contributeEditProfileActivity(): EditProfileActivity

}