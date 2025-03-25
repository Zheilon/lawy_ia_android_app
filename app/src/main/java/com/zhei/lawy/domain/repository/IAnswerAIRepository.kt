package com.zhei.lawy.domain.repository
import kotlinx.coroutines.flow.Flow

interface IAnswerAIRepository {

    suspend fun getAnswerWithFlow(): Flow<Any?>

    suspend fun isCurrent(): Flow<Boolean?>

}