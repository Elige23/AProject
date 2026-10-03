package com.example.aproject.feature.advice.domain.repository

import com.example.aproject.feature.advice.domain.model.Advice
import kotlinx.coroutines.flow.Flow

/**
 * Repository for advice data operations.
 */
interface AdviceRepository {

    //Room
    /**
     * Saves [advice] to the local storage.
     */
    suspend fun insertAdvice(advice: Advice)

    /**
     * Emits the list of saved advices, newest first.
     */
    fun getAllAdvices(): Flow<List<Advice>>


    //Retrofit
    /**
     * Fetches a random advice, wrapped in [Result].
     */
    suspend fun getRandomAdvice(): Result<Advice>
}