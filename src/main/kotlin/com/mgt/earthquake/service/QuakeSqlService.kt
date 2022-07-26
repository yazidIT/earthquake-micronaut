package com.mgt.earthquake.service

import com.mgt.earthquake.jooqmodel.tables.records.QuakeRecord
import com.mgt.earthquake.model.QuakeDTO
import com.mgt.earthquake.model.QuakeModel
import com.mgt.earthquake.model.QuakeResponse
import com.mgt.earthquake.model.QuakeSqlModel
import kotlinx.coroutines.flow.Flow

interface QuakeSqlService {
    fun create(quakedto: QuakeDTO): QuakeRecord
    fun createList(quakeList: List<QuakeDTO>)
}