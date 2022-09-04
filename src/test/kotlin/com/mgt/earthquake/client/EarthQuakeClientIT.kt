package com.mgt.earthquake.client

import io.kotest.core.spec.style.FunSpec
import io.micronaut.test.extensions.kotest5.annotation.MicronautTest
import kotlinx.coroutines.flow.toList
import org.junit.jupiter.api.Assertions
import org.slf4j.LoggerFactory
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@MicronautTest(transactional = true, environments = ["integrationtest"])
class EarthQuakeClientIT(

    var underTest: EarthQuakeClient

) : FunSpec({

    val logger = LoggerFactory.getLogger(this::class.java)

    test("GET earthquake client getTopEarthquakeForToday() should work") {

        // given
        val starttime = OffsetDateTime.of(2022, 5, 1, 0, 0, 0, 0, ZoneOffset.UTC).minusDays(2)
        val starttimestring  = starttime.format(DateTimeFormatter.ISO_DATE_TIME)
        val endtime = OffsetDateTime.of(2022, 5, 1, 0, 0, 0, 0, ZoneOffset.UTC).minusDays(1)
        val endtimestring  = endtime.format(DateTimeFormatter.ISO_DATE_TIME)

        val result = underTest.getTopEarthquakeForToday(
            "geojson", 100, 5.0, 10.0,
            starttimestring, endtimestring, "time-asc"
        ).toList()

        // then
        Assertions.assertTrue(result.isNotEmpty())

    }
})
