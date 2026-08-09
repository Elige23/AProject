package com.example.aproject.feature.advice.domain.repository

import com.example.aproject.feature.advice.data.models.AdviceDto
import com.example.aproject.feature.advice.domain.model.Advice
import kotlinx.coroutines.flow.Flow

interface AdviceRepository {

    //Room
    suspend fun insertAdvice(advice: Advice)

    fun getAllAdvices(): Flow<List<Advice>>


    //Retrofit
    suspend fun getRandomAdvice(): Result<Advice>

}