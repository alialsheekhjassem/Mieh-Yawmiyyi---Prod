package com.magma.miyyiyawmiyyi.android.data.local.repository.dao

import androidx.room.*
import com.magma.miyyiyawmiyyi.android.model.Country

@Dao
interface CountryDao {

    //Room auto generate the code for these methods
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: Country)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: List<Country>): LongArray

    @Query("SELECT * FROM country")
    fun loadAll(): List<Country>

    @Query("SELECT * FROM country WHERE _id LIKE :id")
    fun load(id: String): Country

    @Delete
    fun delete(item: Country?)

    @Query("DELETE FROM country")
    fun deleteAll()

    @Update
    fun update(item: Country?)

    @Update
    fun updateAll(items: List<Country>)

}