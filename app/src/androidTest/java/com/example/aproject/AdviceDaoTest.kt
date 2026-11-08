package com.example.aproject

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.aproject.core.database.AppDatabase
import com.example.aproject.core.database.dao.AdviceDao
import com.example.aproject.core.database.entities.AdviceEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * Instrumented tests for [AdviceDao].
 *
 * Verifies that the DAO correctly inserts data and returns it sorted
 * by creation time in descending order.
 */
@RunWith(AndroidJUnit4::class)
class AdviceDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: AdviceDao

    @Before
    fun createDb() {
        // Create an in-memory database to avoid affecting the real database.
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.adviceDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        // Close the database after each test so that the tests are isolated.
        database.close()
    }

    @Test
    fun insertAdvice_saves_advice_and_getAllAdvices_returns_it() = runTest {
        // Prepare data
        val advice = AdviceEntity(advice = "Test advice", timeCreation = 1000L)

        // Insert into database
        dao.insertAdvice(advice)

        // Retrieve from database
        val result = dao.getAllAdvices().first()

        // Verify
        assertEquals(1, result.size)
        assertEquals("Test advice", result[0].advice)
    }

    @Test
    fun getAllAdvices_returns_list_sorted_by_timeCreation_descending() = runTest {
        // Prepare data with different timestamps
        val olderAdvice = AdviceEntity(advice = "Older advice", timeCreation = 1000L)
        val newerAdvice = AdviceEntity(advice = "Newer advice", timeCreation = 2000L)
        val betweenAdvice = AdviceEntity(advice = "Between advice", timeCreation = 1500L)

        // Insert in random order
        dao.insertAdvice(olderAdvice)
        dao.insertAdvice(newerAdvice)
        dao.insertAdvice(betweenAdvice)

        // Retrieve from database
        val result = dao.getAllAdvices().first()

        // Verify sorting (newest first)
        assertEquals(3, result.size)
        assertEquals("Newer advice", result[0].advice) // 2000L goes first
        assertEquals("Older advice", result[2].advice) // 1000L goes last
    }
}