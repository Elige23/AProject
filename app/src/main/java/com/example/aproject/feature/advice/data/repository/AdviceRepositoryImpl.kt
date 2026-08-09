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

@Singleton
class AdviceRepositoryImpl @Inject constructor(
    private val dao: AdviceDao,
    private val api: AdviceApi
): AdviceRepository {

    fun AdviceEntity.toAdvice(): Advice {
        return Advice(this.id, this.advice, this.timeCreation)
    }
    fun Advice.toAdviceEntity(): AdviceEntity {
        return AdviceEntity(this.id, this.advice, this.timeCreation)
    }

    //Room
    override suspend fun insertAdvice(advice: Advice) = dao.insertAdvice(advice.toAdviceEntity())

    override fun getAllAdvices(): Flow<List<Advice>> = dao.getAllAdvices().map { list ->
        list.map { it.toAdvice() }
    }


    //Retrofit
    fun AdviceDto.toAdvice(): Advice {
        return Advice(advice = this.advice)
    }

    override suspend fun getRandomAdvice(): Result<Advice> {

        //Без Result и блока try-catch небезопасно, так как если например нет интернета, то краш
        return try {

            val response = api.getRandomAdvice()
            val advice = response.slip.toAdvice()
            Result.success(advice)
        }
        catch (e: Exception) {
            Result.failure(e)  // ← Не крашит!
        }

    }
}