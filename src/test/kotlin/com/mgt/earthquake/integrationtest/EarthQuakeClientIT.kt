package com.mgt.earthquake.integrationtest

import com.mgt.earthquake.client.EarthQuakeClient
import io.kotest.core.spec.style.FunSpec
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import kotlinx.coroutines.flow.first
import org.junit.jupiter.api.Assertions
import org.slf4j.LoggerFactory
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@MicronautTest(startApplication = false, environments = ["integrationtest"])
class EarthQuakeClientIT(

    var underTest: EarthQuakeClient,

    ) : FunSpec({

    val logger = LoggerFactory.getLogger(EarthQuakeClientIT::class.java)

    test("GET earthquake client should be able to get quake list") {

        // given
        val starttime = OffsetDateTime.of(2022, 5, 1, 8, 15, 5, 674000000, ZoneOffset.of("+01:00"))
        val starttimestring  = starttime.format(DateTimeFormatter.ISO_DATE_TIME)
        val endtime = OffsetDateTime.of(2022, 5, 2, 8, 15, 5, 674000000, ZoneOffset.of("+01:00"))
        val endtimestring  = endtime.format(DateTimeFormatter.ISO_DATE_TIME)

        // when

        val result = underTest.getTopEarthquakeForToday(
            "geojson", 100, 5.0, 10.0,
            starttimestring, endtimestring, "time-asc"
        ).first()

        // then
        Assertions.assertNotNull(result)
        Assertions.assertTrue(result.features.isNotEmpty())
        logger.info("$result")
    }

})
