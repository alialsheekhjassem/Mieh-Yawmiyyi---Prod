package com.magma.miyyiyawmiyyi.android.data.local.repository.dao

import androidx.room.*
import com.magma.miyyiyawmiyyi.android.model.TaskObj

@Dao
interface TaskDao {

    //Room auto generate the code for these methods
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: TaskObj)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: List<TaskObj>): LongArray

    @Query("SELECT * FROM task_obj")
    fun loadAll(): List<TaskObj>

    @Query("SELECT * FROM task_obj WHERE type = :type")
    fun loadAll(type: String): List<TaskObj>

    @Query("SELECT * FROM task_obj WHERE smTask LIKE :text")
    fun loadByName(text: String): List<TaskObj>

    @Query("SELECT * FROM task_obj WHERE smTask LIKE :text AND type =:type")
    fun loadByName(text: String, type: String): List<TaskObj>

    @Query("SELECT * FROM task_obj WHERE _id LIKE :id")
    fun load(id: String): TaskObj

    @Query("DELETE FROM task_obj WHERE _id LIKE :id")
    fun delete(id: String)

    @Delete
    fun delete(item: TaskObj?)

    @Query("DELETE FROM task_obj")
    fun deleteAll()

    @Update
    fun update(item: TaskObj?)

    @Update
    fun updateAll(items: List<TaskObj>)

}