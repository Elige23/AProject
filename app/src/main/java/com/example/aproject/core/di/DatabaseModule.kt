package com.example.aproject.core.di

import android.content.Context
import androidx.room.Room
import com.example.aproject.core.database.AppDatabase
import com.example.aproject.core.database.dao.AdviceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module that provides the Room database and its DAOs.
 *
 * Note: `fallbackToDestructiveMigration(true)` is used for development only.
 * Replace with proper migrations before releasing to production.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Provides the singleton [AppDatabase] instance backed by the `app_database` file.
     */
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "app_database")
            .fallbackToDestructiveMigration(true)
            .build() // !!!For development purposes only!!! .fallbackToDestructiveMigration(true)
    }

    /**
     * Provides the DAO for accessing the `advice` table.
     */
    @Provides
    @Singleton
    fun provideAdviceDao(database: AppDatabase): AdviceDao {
        return database.adviceDao()
    }
}