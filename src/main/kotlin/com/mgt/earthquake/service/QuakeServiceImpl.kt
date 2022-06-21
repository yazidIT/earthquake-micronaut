package com.mgt.earthquake.service

import com.mgt.earthquake.dao.QuakeRepository
import com.mgt.earthquake.model.QuakeDTO
import com.mgt.earthquake.model.QuakeModel
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId

@Singleton
class QuakeServiceImpl (
    private val quakeRepo: QuakeRepository
) : QuakeService {

    override suspend fun create(quakedto: QuakeDTO): QuakeModel {

        val quakeitem = QuakeModel(
            id = ObjectId(),
            title = quakedto.title,
            magnitude = quakedto.magnitude,
            quaketime = quakedto.quaketime,
            latitude = quakedto.latitude,
            longitude = quakedto.longitude,
            quakeid = quakedto.quakeid
        )

        return quakeRepo.save(quakeitem)
    }

    override suspend fun createList(quakeList: List<QuakeDTO>): List<QuakeModel> {

        val createlist = quakeList
            .map {
                QuakeModel(
                    id = ObjectId(),
                    title = it.title,
                    magnitude = it.magnitude,
                    quaketime = it.quaketime,
                    latitude = it.latitude,
                    longitude = it.longitude,
                    quakeid = it.quakeid
                )
            }

        return quakeRepo.saveAll(createlist).toList()
    }
}