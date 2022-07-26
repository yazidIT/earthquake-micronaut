package com.mgt.earthquake.service

import com.mgt.earthquake.dao.QuakeSqlRepository
import com.mgt.earthquake.jooqmodel.tables.pojos.Quake
import com.mgt.earthquake.jooqmodel.tables.records.QuakeRecord
import com.mgt.earthquake.model.QuakeDTO
import jakarta.inject.Singleton
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.YearMonth
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Singleton
class QuakeSqlServiceImpl (
    private val quakeRepo: QuakeSqlRepository
) : QuakeSqlService {

    override fun create(quakedto: QuakeDTO): QuakeRecord {

        val quakeitem = Quake(
            title = quakedto.title,
            magnitude = quakedto.magnitude,
            quaketime = quakedto.quaketime,
            latitude = quakedto.latitude,
            longitude = quakedto.longitude,
            quakeid = quakedto.quakeid
        )

        return quakeRepo.create(quakeitem)
    }

    override fun createList(quakeList: List<QuakeDTO>) {

        val createlist = quakeList
            .map {
                Quake(
                    title = it.title,
                    magnitude = it.magnitude,
                    quaketime = it.quaketime,
                    latitude = it.latitude,
                    longitude = it.longitude,
                    quakeid = it.quakeid
                )
            }

        quakeRepo.createQuakes(createlist)
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