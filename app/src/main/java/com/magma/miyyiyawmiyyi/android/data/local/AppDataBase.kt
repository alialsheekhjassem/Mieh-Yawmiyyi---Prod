package com.magma.miyyiyawmiyyi.android.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.magma.miyyiyawmiyyi.android.data.local.repository.dao.GiftCardDao
import com.magma.miyyiyawmiyyi.android.data.local.repository.dao.PurchaseCardDao
import com.magma.miyyiyawmiyyi.android.data.local.repository.dao.TaskDao
import com.magma.miyyiyawmiyyi.android.model.GiftCard
import com.magma.miyyiyawmiyyi.android.model.PurchaseCard
import com.magma.miyyiyawmiyyi.android.model.TaskObj
import com.magma.miyyiyawmiyyi.android.model.converters.*

@Database(
    entities = [TaskObj::class, GiftCard::class, PurchaseCard::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    TitleConverter::class, TaskConverter::class,
    AnyConverter::class, AppConverter::class,
    QuizTaskConverter::class,
    GiftConverter::class,
    NameConverter::class,
    PrizeConverter::class,
    ConfigConverter::class,
    FixedStartDrawConverter::class,
    CancellationConverter::class,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun giftCardDao(): GiftCardDao
    abstract fun purchaseCardDao(): PurchaseCardDao
}