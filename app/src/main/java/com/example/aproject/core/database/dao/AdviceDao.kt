package com.example.aproject.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.aproject.core.database.entities.AdviceEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for the `advice` table.
 */
@Dao
interface AdviceDao {

    /**
     * Inserts [advice], replacing any existing row with the same id.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAdvice(advice: AdviceEntity)

    /**
     * Returns a reactive stream of all saved advices, sorted by creation time
     * in descending order (from newest to oldest).
     */
    @Query("SELECT * FROM advice ORDER BY time_creation DESC")
    fun getAllAdvices(): Flow<List<AdviceEntity>>
}