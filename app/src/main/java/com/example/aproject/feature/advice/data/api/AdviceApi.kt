package com.example.aproject.feature.advice.data.api

import com.example.aproject.feature.advice.data.models.AdviceResponse
import retrofit2.http.GET

/**
 * Retrofit interface for the Advice Slip API.
 *
 * @see <a href="https://api.adviceslip.com/">Advice Slip API</a>
 */
interface AdviceApi {

    /**
     * Fetches a random advice.
     *
     * @return [AdviceResponse] containing the advice slip.
     */
    @GET("advice")  // ← https://api.adviceslip.com/advice
    suspend fun getRandomAdvice(): AdviceResponse
}