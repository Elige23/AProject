package com.example.aproject.feature.advice.data.api

import com.example.aproject.feature.advice.data.models.AdviceResponse
import retrofit2.http.GET

// Advice Slip API использует HTTPS, поэтому android:usesCleartextTraffic="true"  <!-- Только для HTTP! -->
// не нужно добавлять в манифесть

interface AdviceApi {

    @GET("advice")  // ← https://api.adviceslip.com/advice
    suspend fun getRandomAdvice(): AdviceResponse
}