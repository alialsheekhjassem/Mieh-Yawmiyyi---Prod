package com.magma.miyyiyawmiyyi.android.data.local.repository.dao

import androidx.room.*
import com.magma.miyyiyawmiyyi.android.model.Ticket

@Dao
interface TicketDao {

    //Room auto generate the code for these methods
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: Ticket)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: List<Ticket>): LongArray

    @Query("SELECT * FROM ticket")
    fun loadAll(): List<Ticket>

    @Query("SELECT * FROM ticket WHERE _id LIKE :id")
    fun load(id: String): Ticket

    @Delete
    fun delete(item: Ticket?)

    @Query("DELETE FROM ticket")
    fun deleteAll()

    @Update
    fun update(item: Ticket?)

    @Update
    fun updateAll(items: List<Ticket>)

}