package com.example.aproject.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.aproject.core.database.dao.AdviceDao
import com.example.aproject.core.database.entities.AdviceEntity

/**
 * Room database schema of the application.
 *
 * Registers the following entities:
 * - [AdviceEntity] — stores saved advices.
 *
 * The physical database file is created via `Room.databaseBuilder(...)` in `DatabaseModule`.
 * Increment `version` and add a migration when the schema changes.
 */
@Database(entities = [AdviceEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Provides access to the `advice` table.
     */
    abstract fun adviceDao(): AdviceDao
}