package com.magma.miyyiyawmiyyi.android.data.local.repository.dao

import androidx.room.*
import com.magma.miyyiyawmiyyi.android.model.GiftCard

@Dao
interface GiftCardDao {

    //Room auto generate the code for these methods
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: GiftCard)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: List<GiftCard>): LongArray

    @Query("SELECT * FROM gift_card")
    fun loadAll(): List<GiftCard>

    @Query("SELECT * FROM gift_card WHERE _id LIKE :id")
    fun load(id: String): GiftCard

    @Delete
    fun delete(item: GiftCard?)

    @Query("DELETE FROM gift_card")
    fun deleteAll()

    @Update
    fun update(item: GiftCard?)

    @Update
    fun updateAll(items: List<GiftCard>)

}