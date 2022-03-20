package com.magma.miyyiyawmiyyi.android.data.local.repository.dao

import androidx.room.*
import com.magma.miyyiyawmiyyi.android.model.Round

@Dao
interface RoundDao {

    //Room auto generate the code for these methods
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: Round)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: List<Round>): LongArray

    @Query("SELECT * FROM round_card")
    fun loadAll(): List<Round>

    @Query("SELECT * FROM round_card WHERE _id LIKE :id")
    fun load(id: String): Round

    @Delete
    fun delete(item: Round?)

    @Query("DELETE FROM round_card")
    fun deleteAll()

    @Update
    fun update(item: Round?)

    @Update
    fun updateAll(items: List<Round>)

}