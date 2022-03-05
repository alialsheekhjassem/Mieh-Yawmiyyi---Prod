package com.magma.miyyiyawmiyyi.android.di.module

import androidx.lifecycle.ViewModel
import com.magma.miyyiyawmiyyi.android.utils.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import com.magma.miyyiyawmiyyi.android.presentation.home.ui.tasks.TasksViewModel
import com.magma.miyyiyawmiyyi.android.presentation.details.RestaurantDetailsViewModel
import com.magma.miyyiyawmiyyi.android.presentation.details.address.AddressViewModel
import com.magma.miyyiyawmiyyi.android.presentation.details.cart.CartViewModel
import com.magma.miyyiyawmiyyi.android.presentation.home.ui.home.HomeViewModel
import com.magma.miyyiyawmiyyi.android.presentation.home.ui.invitations.InvitationsViewModel
import com.magma.miyyiyawmiyyi.android.presentation.home.ui.live_stream.LiveStreamViewModel
import com.magma.miyyiyawmiyyi.android.presentation.home.ui.our_store.OurStoreViewModel
import com.magma.miyyiyawmiyyi.android.presentation.home.ui.profile.ProfileViewModel
import com.magma.miyyiyawmiyyi.android.presentation.home.ui.quizzes.QuizzesViewModel
import com.magma.miyyiyawmiyyi.android.presentation.home.ui.tickets.TicketsViewModel
import com.magma.miyyiyawmiyyi.android.presentation.home.ui.settings.SettingsViewModel
import com.magma.miyyiyawmiyyi.android.presentation.onboarding.OnBoardingViewModel
import com.magma.miyyiyawmiyyi.android.presentation.registration.check_code.CheckCodeViewModel
import com.magma.miyyiyawmiyyi.android.presentation.registration.finish_account.FinishAccountViewModel
import com.magma.miyyiyawmiyyi.android.presentation.registration.invitation_link.InvitationLinkViewModel
import com.magma.miyyiyawmiyyi.android.presentation.registration.login.LoginViewModel
import com.magma.miyyiyawmiyyi.android.presentation.splash.SplashViewModel

// Because of @Binds, ViewModelModule needs to be an abstract class

@Module
abstract class ViewModelModule {

// Use @Binds to tell Dagger which implementation it needs to use when providing an interface.

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindSplashViewModel(viewModel: SplashViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OnBoardingViewModel::class)
    abstract fun bindOnBoardingViewModel(viewModel: OnBoardingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FinishAccountViewModel::class)
    abstract fun bindFinishAccountViewModel(viewModel: FinishAccountViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CheckCodeViewModel::class)
    abstract fun bindCheckCodeViewModel(viewModel: CheckCodeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(InvitationLinkViewModel::class)
    abstract fun bindInvitationLinkViewModel(viewModel: InvitationLinkViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun bindSettingsViewModel(viewModel: SettingsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OurStoreViewModel::class)
    abstract fun bindOurStoreViewModel(viewModel: OurStoreViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(InvitationsViewModel::class)
    abstract fun bindInvitationsViewModel(viewModel: InvitationsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(QuizzesViewModel::class)
    abstract fun bindQuizzesViewModel(viewModel: QuizzesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TasksViewModel::class)
    abstract fun bindDashboardViewModel(viewModel: TasksViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TicketsViewModel::class)
    abstract fun bindNotificationsViewModel(viewModel: TicketsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LiveStreamViewModel::class)
    abstract fun bindMyOrderViewModel(viewModel: LiveStreamViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindProfileViewModel(viewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RestaurantDetailsViewModel::class)
    abstract fun bindRestaurantDetailsViewModel(viewModel: RestaurantDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CartViewModel::class)
    abstract fun bindCartViewModel(viewModel: CartViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddressViewModel::class)
    abstract fun bindAddressViewModel(viewModel: AddressViewModel): ViewModel
    

}