package com.example.aproject.feature.advice.data.repository

import com.example.aproject.core.database.dao.AdviceDao
import com.example.aproject.core.database.entities.AdviceEntity
import com.example.aproject.feature.advice.data.api.AdviceApi
import com.example.aproject.feature.advice.data.models.AdviceDto
import com.example.aproject.feature.advice.domain.model.Advice
import com.example.aproject.feature.advice.domain.repository.AdviceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of [AdviceRepository] backed by the local Room database
 * and the remote Advice Slip API.
 */
@Singleton
class AdviceRepositoryImpl @Inject constructor(
    private val dao: AdviceDao,
    private val api: AdviceApi
): AdviceRepository {

    private fun AdviceEntity.toAdvice(): Advice {
        return Advice(this.id, this.advice, this.timeCreation)
    }
    private fun Advice.toAdviceEntity(): AdviceEntity {
        return AdviceEntity(this.id, this.advice, this.timeCreation)
    }

    // Room
    override suspend fun insertAdvice(advice: Advice) = dao.insertAdvice(advice.toAdviceEntity())

    /**
     * Emits the list of saved advices, newest first.
     */
    override fun getAllAdvices(): Flow<List<Advice>> = dao.getAllAdvices().map { list ->
        list.map { it.toAdvice() }
    }


    // Retrofit
    private fun AdviceDto.toAdvice(): Advice {
        return Advice(advice = this.advice)
    }

    /**
     * Fetches random advice from the API, wrapped in [Result].
     * Without Result and try-catch block it is unsafe, because if, for example, there is no internet, then the crash.
     */
    override suspend fun getRandomAdvice(): Result<Advice> {

        return try {

            val response = api.getRandomAdvice()
            val advice = response.slip.toAdvice()
            Result.success(advice)
        }
        catch (e: Exception) {
            Result.failure(e)
        }
    }
}