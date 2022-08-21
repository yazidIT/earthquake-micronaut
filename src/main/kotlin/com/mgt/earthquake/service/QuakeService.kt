package com.mgt.earthquake.service

import com.mgt.earthquake.model.QuakeDTO
import com.mgt.earthquake.model.QuakeModel
import com.mgt.earthquake.model.QuakeResponse
import kotlinx.coroutines.flow.Flow

interface QuakeService {
    suspend fun create(quakedto: QuakeDTO): QuakeModel
    fun createList(quakeList: List<QuakeDTO>): Flow<QuakeModel>
    fun latestQuake(): Flow<QuakeResponse>
    fun latestNumberOfQuake(number: Int): Flow<QuakeModel>
}