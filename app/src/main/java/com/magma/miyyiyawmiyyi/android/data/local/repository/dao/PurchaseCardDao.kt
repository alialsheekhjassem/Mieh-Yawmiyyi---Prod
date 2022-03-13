package com.magma.miyyiyawmiyyi.android.data.local.repository.dao

import androidx.room.*
import com.magma.miyyiyawmiyyi.android.model.PurchaseCard

@Dao
interface PurchaseCardDao {

    //Room auto generate the code for these methods
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: PurchaseCard)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: List<PurchaseCard>): LongArray

    @Query("SELECT * FROM purchase_card")
    fun loadAll(): List<PurchaseCard>

    @Query("SELECT * FROM purchase_card WHERE _id LIKE :id")
    fun load(id: String): PurchaseCard

    @Delete
    fun delete(item: PurchaseCard?)

    @Query("DELETE FROM purchase_card")
    fun deleteAll()

    @Update
    fun update(item: PurchaseCard?)

    @Update
    fun updateAll(items: List<PurchaseCard>)

}