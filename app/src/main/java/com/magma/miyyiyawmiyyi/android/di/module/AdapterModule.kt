package com.magma.miyyiyawmiyyi.android.di.module

import dagger.Module
import dagger.Provides
import com.magma.miyyiyawmiyyi.android.presentation.details.FoodAdapter
import com.magma.miyyiyawmiyyi.android.presentation.details.cart.CartAdapter
import com.magma.miyyiyawmiyyi.android.presentation.home.ui.home.AdapterSliderView
import com.magma.miyyiyawmiyyi.android.presentation.home.ui.home.WinnersAdapter
import com.magma.miyyiyawmiyyi.android.presentation.home.ui.notifications.NotificationsAdapter
import com.magma.miyyiyawmiyyi.android.presentation.home.ui.orders.OrdersAdapter
import com.magma.miyyiyawmiyyi.android.presentation.home.ui.our_store.GiftCardsAdapter
import com.magma.miyyiyawmiyyi.android.presentation.home.ui.our_store.PurchaseCardsAdapter
import com.magma.miyyiyawmiyyi.android.presentation.home.ui.tasks.TasksAdapter
import com.magma.miyyiyawmiyyi.android.presentation.home.ui.tickets.TicketsAdapter


@Module
class AdapterModule {

    @Provides
    fun provideAdapterSliderView(): AdapterSliderView {
        return AdapterSliderView()
    }

    @Provides
    fun provideNotificationsAdapter(): NotificationsAdapter {
        return NotificationsAdapter()
    }

    @Provides
    fun provideWinnersAdapter(): WinnersAdapter {
        return WinnersAdapter()
    }

    @Provides
    fun provideTicketsAdapter(): TicketsAdapter {
        return TicketsAdapter()
    }

    @Provides
    fun provideTasksAdapter(): TasksAdapter {
        return TasksAdapter()
    }

    @Provides
    fun provideFoodsAdapter(): FoodAdapter {
        return FoodAdapter()
    }

    @Provides
    fun provideGiftCardsAdapter(): GiftCardsAdapter {
        return GiftCardsAdapter()
    }

    @Provides
    fun providePurchaseCardsAdapter(): PurchaseCardsAdapter {
        return PurchaseCardsAdapter()
    }

    @Provides
    fun provideOrdersAdapter(): OrdersAdapter {
        return OrdersAdapter()
    }

    @Provides
    fun provideCartAdapter(): CartAdapter {
        return CartAdapter()
    }

}