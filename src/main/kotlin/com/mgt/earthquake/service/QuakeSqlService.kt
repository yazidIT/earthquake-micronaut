package com.mgt.earthquake.service

import com.mgt.earthquake.jooqmodel.tables.records.QuakeRecord
import com.mgt.earthquake.model.QuakeDTO

interface QuakeSqlService {
    suspend fun create(quakedto: QuakeDTO): QuakeRecord?
    suspend fun createList(quakeList: List<QuakeDTO>)
}