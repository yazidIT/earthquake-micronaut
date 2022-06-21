package com.mgt.earthquake.service

import com.mgt.earthquake.model.QuakeDTO
import com.mgt.earthquake.model.QuakeModel

interface QuakeService {
    suspend fun create(quakedto: QuakeDTO): QuakeModel
    suspend fun createList(quakeList: List<QuakeDTO>): List<QuakeModel>
}