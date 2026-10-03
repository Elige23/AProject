package com.example.aproject.feature.advice.di

import com.example.aproject.core.di.qualifiers.AdviceApiRetrofit
import com.example.aproject.feature.advice.data.api.AdviceApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Hilt module that provides Retrofit and [AdviceApi] for the Advice Slip API.
 */
@Module
@InstallIn(SingletonComponent::class)
object AdviceRetrofitModule {

    /**
     * Provides a [Retrofit] instance for the Advice Slip API, uses qualifier @AdviceApiRetrofit.
     */
    @AdviceApiRetrofit
    @Provides
    @Singleton
    fun provideAdviceRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.adviceslip.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * Provides the [AdviceApi] implementation, uses qualifier @AdviceApiRetrofit.
     */
    @Provides
    @Singleton
    fun provideAdviceApi(
        @AdviceApiRetrofit retrofit: Retrofit
    ): AdviceApi {
        return retrofit.create(AdviceApi::class.java)
    }
}