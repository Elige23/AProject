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


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun  provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "app_database")
            .fallbackToDestructiveMigration(true)
            .build() //.fallbackToDestructiveMigration()  // !!!For development purposes only!!! .fallbackToDestructiveMigration(true)
    }

    @Provides
    @Singleton
    fun provideAdviceDao(database: AppDatabase): AdviceDao {
        return database.adviceDao()
    }
}