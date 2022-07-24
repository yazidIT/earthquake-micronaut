package com.mgt.earthquake.service

import com.mgt.earthquake.client.EarthQuakeClient
import com.mgt.earthquake.dao.QuakeRepository
import com.mgt.earthquake.model.QuakeDTO
import com.mgt.earthquake.model.QuakeModel
import com.mgt.earthquake.model.QuakeResponse
import jakarta.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.YearMonth
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

@Singleton
class QuakeServiceImpl (
    private val quakeRepo: QuakeRepository,
    private val quakeClient: EarthQuakeClient
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

    override suspend fun latestQuake(): QuakeResponse {

        val response = quakeClient.getTopEarthquakeForToday(
            "geojson", 100, 5.0, 10.0,
            yesterdayDateZeroHundredHoursIso(), yesterdayDateTwentyThreeHundredHoursIso(), "time-asc"
        ).flowOn(Dispatchers.IO).first()

        return response
    }

    fun yesterdayDateZeroHundredHoursIso(): String {

        val todaydatetime = LocalDateTime.now()
        val yesterday: OffsetDateTime

        if(todaydatetime.dayOfMonth == 1) {

            if(todaydatetime.monthValue == 1) {
                yesterday = OffsetDateTime.of(todaydatetime.year - 1, 12, 31,
                    0, 0, 0, 0, ZoneOffset.UTC)

            } else {
                val yearmonth = YearMonth.of(todaydatetime.year, todaydatetime.monthValue)
                yesterday = OffsetDateTime.of(
                    todaydatetime.year,
                    todaydatetime.monthValue - 1,
                    yearmonth.lengthOfMonth(),
                    0, 0, 0, 0, ZoneOffset.UTC
                )
            }

        } else {
            yesterday = OffsetDateTime.of(
                todaydatetime.year,
                todaydatetime.monthValue,
                todaydatetime.dayOfMonth - 1,
                0, 0, 0, 0, ZoneOffset.UTC
            )
        }

        return yesterday.format(DateTimeFormatter.ISO_DATE_TIME)
    }

    fun yesterdayDateTwentyThreeHundredHoursIso(): String {
        val todaydatetime = LocalDateTime.now()
        val yesterday: OffsetDateTime

        if(todaydatetime.dayOfMonth == 1) {

            if(todaydatetime.monthValue == 1) {
                yesterday = OffsetDateTime.of(todaydatetime.year - 1, 12, 31,
                    23, 59, 59, 0, ZoneOffset.UTC)

            } else {
                val yearmonth = YearMonth.of(todaydatetime.year, todaydatetime.monthValue)
                yesterday = OffsetDateTime.of(
                    todaydatetime.year,
                    todaydatetime.monthValue - 1,
                    yearmonth.lengthOfMonth(),
                    23, 59, 59, 0, ZoneOffset.UTC
                )
            }

        } else {
            yesterday = OffsetDateTime.of(
                todaydatetime.year,
                todaydatetime.monthValue,
                todaydatetime.dayOfMonth - 1,
                23, 59, 59, 0, ZoneOffset.UTC
            )
        }

        return yesterday.format(DateTimeFormatter.ISO_DATE_TIME)
    }
}