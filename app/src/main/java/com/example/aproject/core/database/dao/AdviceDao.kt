package com.example.aproject.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.aproject.core.database.entities.AdviceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AdviceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAdvice(advice: AdviceEntity)

    //Sort the list in descending order by creation time (from newest to oldest)
    @Query("SELECT * FROM advice ORDER BY time_creation DESC")
    fun getAllAdvices(): Flow<List<AdviceEntity>>

}