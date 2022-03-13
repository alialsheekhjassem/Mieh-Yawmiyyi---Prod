package com.magma.miyyiyawmiyyi.android.di.module

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.google.gson.Gson
import com.magma.miyyiyawmiyyi.android.utils.Const
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import com.magma.miyyiyawmiyyi.android.data.local.AppDatabase
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideDatabase(
        app: Application
    ) = Room.databaseBuilder(
        app,
        AppDatabase::class.java,
        Const.DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideTaskDao(db: AppDatabase) = db.taskDao()

    @Singleton
    @Provides
    fun provideGiftCardDao(db: AppDatabase) = db.giftCardDao()

    @Singleton
    @Provides
    fun providePurchaseCardDao(db: AppDatabase) = db.purchaseCardDao()

    @Singleton
    @Provides
    fun provideSharedPreferences(app: Application): SharedPreferences {
        return app.getSharedPreferences(Const.PREF_NAME, AppCompatActivity.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideCoroutineContext(): CoroutineContext {
        return Dispatchers.Main
    }

    @Provides
    @Singleton
    fun provideGsonObject(): Gson {
        return Gson()
    }
}