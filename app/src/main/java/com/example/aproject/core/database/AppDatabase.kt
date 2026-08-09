package com.example.aproject.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.aproject.core.database.dao.AdviceDao
import com.example.aproject.core.database.entities.AdviceEntity

@Database(entities = [AdviceEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun adviceDao(): AdviceDao
}