package com.example.aproject.core.di

import com.example.aproject.feature.advice.data.repository.AdviceRepositoryImpl
import com.example.aproject.feature.advice.domain.repository.AdviceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {


    @Binds
    @Singleton
    fun bindAdviceRepository(
            adviceRepository: AdviceRepositoryImpl
    ): AdviceRepository

}