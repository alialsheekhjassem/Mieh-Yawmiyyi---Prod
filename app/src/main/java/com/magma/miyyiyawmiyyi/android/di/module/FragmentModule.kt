package com.magma.miyyiyawmiyyi.android.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import  com.magma.miyyiyawmiyyi.android.presentation.details.FoodMenuFragment
import  com.magma.miyyiyawmiyyi.android.presentation.home.ui.tasks.TasksFragment
import  com.magma.miyyiyawmiyyi.android.presentation.details.RestaurantDetailsFragment
import  com.magma.miyyiyawmiyyi.android.presentation.details.address.AddressFragment
import  com.magma.miyyiyawmiyyi.android.presentation.details.cart.CartFragment
import com.magma.miyyiyawmiyyi.android.presentation.home.ui.about_us.AboutUsFragment
import com.magma.miyyiyawmiyyi.android.presentation.home.ui.confirm_task.ConfirmEndTaskFragment
import  com.magma.miyyiyawmiyyi.android.presentation.home.ui.home.HomeFragment
import com.magma.miyyiyawmiyyi.android.presentation.home.ui.invitations.InvitationsFragment
import  com.magma.miyyiyawmiyyi.android.presentation.home.ui.live_stream.LiveStreamFragment
import com.magma.miyyiyawmiyyi.android.presentation.home.ui.notifications.NotificationsFragment
import com.magma.miyyiyawmiyyi.android.presentation.home.ui.orders.OrdersFragment
import com.magma.miyyiyawmiyyi.android.presentation.home.ui.our_store.OurStoreFragment
import  com.magma.miyyiyawmiyyi.android.presentation.home.ui.profile.ProfileFragment
import com.magma.miyyiyawmiyyi.android.presentation.home.ui.quizzes.QuizzesFragment
import  com.magma.miyyiyawmiyyi.android.presentation.home.ui.tickets.TicketsFragment
import com.magma.miyyiyawmiyyi.android.presentation.home.ui.settings.SettingsFragment
import com.magma.miyyiyawmiyyi.android.presentation.onboarding.GetStartedFragment
import  com.magma.miyyiyawmiyyi.android.presentation.onboarding.OnBoardingFragment
import com.magma.miyyiyawmiyyi.android.presentation.onboarding.OnBoardingPagerFragment
import  com.magma.miyyiyawmiyyi.android.presentation.registration.check_code.CheckCodeFragment
import com.magma.miyyiyawmiyyi.android.presentation.registration.finish_account.FinishAccountFragment
import com.magma.miyyiyawmiyyi.android.presentation.registration.invitation_link.InvitationFragment
import  com.magma.miyyiyawmiyyi.android.presentation.registration.login.LoginFragment

@Module
internal abstract class FragmentModule {

    /*OnBoarding*/
    @ContributesAndroidInjector
    abstract fun contributeOnBoardingFragment(): OnBoardingFragment

    @ContributesAndroidInjector
    abstract fun contributeGetStartedFragment(): GetStartedFragment

    @ContributesAndroidInjector
    abstract fun contributeOnBoardingPagerFragment(): OnBoardingPagerFragment


    /*Account*/
    @ContributesAndroidInjector
    abstract fun contributeFinishAccountFragment(): FinishAccountFragment

    @ContributesAndroidInjector
    abstract fun contributeLoginFragment(): LoginFragment

    @ContributesAndroidInjector
    abstract fun contributeCheckCodeFragment(): CheckCodeFragment

    @ContributesAndroidInjector
    abstract fun contributeInvitationFragment(): InvitationFragment


    /*Home*/
    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributeSettingsFragment(): SettingsFragment

    @ContributesAndroidInjector
    abstract fun contributeAboutUsFragment(): AboutUsFragment

    @ContributesAndroidInjector
    abstract fun contributeDashboardFragment(): TasksFragment

    @ContributesAndroidInjector
    abstract fun contributeTicketsFragment(): TicketsFragment

    @ContributesAndroidInjector
    abstract fun contributeMyOrderFragment(): LiveStreamFragment

    @ContributesAndroidInjector
    abstract fun contributeProfileFragment(): ProfileFragment

    @ContributesAndroidInjector
    abstract fun contributeOurStoreFragment(): OurStoreFragment

    @ContributesAndroidInjector
    abstract fun contributeInvitationsFragment(): InvitationsFragment

    @ContributesAndroidInjector
    abstract fun contributeQuizzesFragment(): QuizzesFragment

    @ContributesAndroidInjector
    abstract fun contributeRestaurantDetailsFragment(): RestaurantDetailsFragment

    @ContributesAndroidInjector
    abstract fun contributeFoodMenuFragment(): FoodMenuFragment

    @ContributesAndroidInjector
    abstract fun contributeCartFragment(): CartFragment

    @ContributesAndroidInjector
    abstract fun contributeAddressFragment(): AddressFragment

    @ContributesAndroidInjector
    abstract fun contributeOrdersFragment(): OrdersFragment

    @ContributesAndroidInjector
    abstract fun contributeNotificationsFragment(): NotificationsFragment

    @ContributesAndroidInjector
    abstract fun contributeConfirmEndTaskFragment(): ConfirmEndTaskFragment

}