package com.zhei.lawy.domain.repository
import kotlinx.coroutines.flow.Flow

interface IQuestionRepository {

    suspend fun updateQuestion (map: Map<String, Any>)

    suspend fun isActivate () : Flow<Boolean?>

}