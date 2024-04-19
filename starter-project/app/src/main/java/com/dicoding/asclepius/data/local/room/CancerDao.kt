package com.dicoding.asclepius.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.asclepius.data.local.entity.CancerEntity

@Dao
interface CancerDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(cancer: CancerEntity)

    @Delete
    fun delete(cancer: CancerEntity)

    @Query("SELECT * FROM cancer order by cancer ASC")
    fun getAll(): LiveData<List<CancerEntity>>
}