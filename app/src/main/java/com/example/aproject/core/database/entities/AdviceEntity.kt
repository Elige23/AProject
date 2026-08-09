package com.example.aproject.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "advice")
data class AdviceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val advice: String,
    @ColumnInfo(name = "time_creation")
    val timeCreation: Long = System.currentTimeMillis()
)
