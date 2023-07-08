package com.mgt.earthquake.service

import com.mgt.earthquake.model.QuakeDTO
import com.mgt.earthquake.model.QuakeModelDTO
import com.mgt.earthquake.model.QuakeResponse
import kotlinx.coroutines.flow.Flow

interface QuakeService {
    suspend fun create(quakedto: QuakeDTO): QuakeModelDTO?
    fun createList(quakeList: List<QuakeDTO>): Flow<QuakeModelDTO>
    fun latestQuake(): Flow<QuakeResponse>
    fun latestNumberOfQuake(number: Int): Flow<QuakeModelDTO>
}