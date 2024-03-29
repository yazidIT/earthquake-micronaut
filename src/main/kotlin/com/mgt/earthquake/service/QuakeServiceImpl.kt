package com.mgt.earthquake.service

import com.mgt.earthquake.client.EarthQuakeClient
import com.mgt.earthquake.dao.QuakeRepository
import com.mgt.earthquake.model.*
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.YearMonth
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Singleton
class QuakeServiceImpl (
    private val quakeRepo: QuakeRepository,
    private val quakeClient: EarthQuakeClient
) : QuakeService {

    override suspend fun create(quakedto: QuakeDTO): QuakeModelDTO? =

        QuakeModel(
            title = quakedto.title,
            magnitude = quakedto.magnitude,
            quaketime = quakedto.quaketime,
            latitude = quakedto.latitude,
            longitude = quakedto.longitude,
            quakeid = quakedto.quakeid
        ).let {
            quakeRepo.save(it)?.let { quakeModel -> quakeModel.toDTO() }
        }


    override fun createList(quakeList: List<QuakeDTO>): Flow<QuakeModelDTO> = flow {

        quakeList
            .map {
                QuakeModel(
                    title = it.title,
                    magnitude = it.magnitude,
                    quaketime = it.quaketime,
                    latitude = it.latitude,
                    longitude = it.longitude,
                    quakeid = it.quakeid
                )
            }
            .toList().apply {
                quakeRepo.saveAll(this)
                    .collect { emit(it.toDTO()) }
            }

    }

    override fun latestQuake(): Flow<QuakeResponse> =

        quakeClient.getTopEarthquakeForToday(
            "geojson", 100, 5.0, 10.0,
            yesterdayDateZeroHundredHoursIso(), yesterdayDateTwentyThreeHundredHoursIso(), "time-asc")

    override fun latestNumberOfQuake(number: Int): Flow<QuakeModelDTO> = flow {
        quakeRepo.findLatestNumber(number)
            .collect { emit(it.toDTO()) }
    }


    /***
     * Get yesterday's 00:00 at GMT
     */
    fun getZeroHundredHourIso(targetDate: LocalDateTime): String {

        val yesterday: OffsetDateTime

        when(targetDate.dayOfMonth) {
            1 -> {
                yesterday = when(targetDate.monthValue) {
                    1 -> {
                        OffsetDateTime.of(targetDate.year - 1, 12, 31,
                            0, 0, 0, 0, ZoneOffset.UTC)
                    }
                    else -> {
                        val yearmonth = YearMonth.of(targetDate.year, targetDate.monthValue - 1)
                        OffsetDateTime.of(
                            targetDate.year,
                            targetDate.monthValue - 1,
                            yearmonth.lengthOfMonth(),
                            0, 0, 0, 0, ZoneOffset.UTC
                        )
                    }
                }
            }
            else -> {
                yesterday = OffsetDateTime.of(
                    targetDate.year,
                    targetDate.monthValue,
                    targetDate.dayOfMonth - 1,
                    0, 0, 0, 0, ZoneOffset.UTC
                )
            }
        }

        return yesterday.format(DateTimeFormatter.ISO_DATE_TIME)
    }

    fun yesterdayDateZeroHundredHoursIso() = getZeroHundredHourIso(LocalDateTime.now())


    /***
     * Get yesterday's 23:59 at GMT
     */
    fun getTwentyThreeHundredHourIso(targetDate: LocalDateTime): String {

        val yesterday: OffsetDateTime

        when(targetDate.dayOfMonth) {
            1 -> {
                yesterday = when(targetDate.monthValue) {
                    1 -> {
                        OffsetDateTime.of(targetDate.year - 1, 12, 31,
                            23, 59, 59, 0, ZoneOffset.UTC)
                    }
                    else -> {
                        val yearmonth = YearMonth.of(targetDate.year, targetDate.monthValue - 1)
                        OffsetDateTime.of(
                            targetDate.year,
                            targetDate.monthValue - 1,
                            yearmonth.lengthOfMonth(),
                            23, 59, 59, 0, ZoneOffset.UTC
                        )
                    }
                }
            }
            else -> {
                yesterday = OffsetDateTime.of(
                    targetDate.year,
                    targetDate.monthValue,
                    targetDate.dayOfMonth - 1,
                    23, 59, 59, 0, ZoneOffset.UTC
                )
            }
        }

        return yesterday.format(DateTimeFormatter.ISO_DATE_TIME)
    }

    fun yesterdayDateTwentyThreeHundredHoursIso() = getTwentyThreeHundredHourIso(LocalDateTime.now())
}