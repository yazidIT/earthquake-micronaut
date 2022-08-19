package com.mgt.earthquake.client

import com.mgt.earthquake.model.QuakeGeometry
import com.mgt.earthquake.model.QuakeProperty
import com.mgt.earthquake.model.QuakeResponse
import com.mgt.earthquake.model.QuakeResponseFeature
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.mockserver.MockServerListener
import io.micronaut.serde.ObjectMapper
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.assertThrows
import org.mockserver.client.MockServerClient
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import org.mockserver.model.MediaType
import org.mockserver.model.Parameter
import org.slf4j.LoggerFactory
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@MicronautTest
class EarthQuakeClientTest(

    var underTest: EarthQuakeClient,
    var objectMapper: ObjectMapper

) : FunSpec({

    val logger = LoggerFactory.getLogger(EarthQuakeClientTest::class.java)

    listener(MockServerListener(5000))
    val clientAndServer = MockServerClient("localhost", 5000)

    test("GET earthquake client should work") {

        clientAndServer.reset()

        // given
        val starttime = OffsetDateTime.of(2022, 5, 1, 8, 15, 5, 674000000, ZoneOffset.of("+01:00"))
        val starttimestring  = starttime.format(DateTimeFormatter.ISO_DATE_TIME)
        val endtime = OffsetDateTime.of(2022, 5, 2, 8, 15, 5, 674000000, ZoneOffset.of("+01:00"))
        val endtimestring  = endtime.format(DateTimeFormatter.ISO_DATE_TIME)
        val quakeresponse = QuakeResponse(features = listOf(
                                    QuakeResponseFeature(id = "1234",
                                        properties = QuakeProperty(time = 12345678L, title = "quakeproperty", mag = 5.67),
                                        geometry = QuakeGeometry(coordinates = arrayListOf(6.7, 7.8, 9.2)))
                                )
                            )
        val mockdata = flowOf(quakeresponse).first()

        val jsonstring = objectMapper.writeValueAsString(mockdata)

        // when
        clientAndServer
            .`when`(
                HttpRequest.request()
                    .withMethod("GET")
                    .withPath("/fdsnws/event/1/query")
                    .withQueryStringParameters(
                        Parameter.param("format", "geojson"),
                        Parameter.param("limit", "100"),
                        Parameter.param("minmagnitude", "5.0"),
                        Parameter.param("maxmagnitude", "10.0"),
                        Parameter.param("starttime", starttimestring),
                        Parameter.param("endtime", endtimestring),
                        Parameter.param("orderby", "time-asc")
                    )
            )
            .respond(
                HttpResponse.response()
                    .withStatusCode(200)
                    .withContentType(MediaType.APPLICATION_JSON)
                    .withBody(jsonstring)
            )

        val result = underTest.getTopEarthquakeForToday(
            "geojson", 100, 5.0, 10.0,
            starttimestring, endtimestring, "time-asc"
        ).first()

        // then
        Assertions.assertNotNull(result)
        Assertions.assertTrue(result.features.isNotEmpty())
        logger.info("$result")
    }

    test("GET earthquake return 401 should throw exception") {

        clientAndServer.reset()

        // given
        val starttime = OffsetDateTime.of(2022, 5, 1, 8, 15, 5, 674000000, ZoneOffset.of("+01:00"))
        val starttimestring  = starttime.format(DateTimeFormatter.ISO_DATE_TIME)
        val endtime = OffsetDateTime.of(2022, 5, 2, 8, 15, 5, 674000000, ZoneOffset.of("+01:00"))
        val endtimestring  = endtime.format(DateTimeFormatter.ISO_DATE_TIME)

        // when
        clientAndServer
            .`when`(
                HttpRequest.request()
                    .withMethod("GET")
                    .withPath("/fdsnws/event/1/query")
                    .withQueryStringParameters(
                        Parameter.param("format", "geojson"),
                        Parameter.param("limit", "100"),
                        Parameter.param("minmagnitude", "5.0"),
                        Parameter.param("maxmagnitude", "10.0"),
                        Parameter.param("starttime", starttimestring),
                        Parameter.param("endtime", endtimestring),
                        Parameter.param("orderby", "time-asc")
                    )
            )
            .respond(
                HttpResponse.response()
                    .withStatusCode(401)
            )

        assertThrows<Exception> {
            underTest.getTopEarthquakeForToday(
                "geojson", 100, 5.0, 10.0,
                starttimestring, endtimestring, "time-asc"
            ).first()
        }.printStackTrace()

        // then
    }

    test("GET earthquake return 404 should throw exception") {

        clientAndServer.reset()

        // given
        val starttime = OffsetDateTime.of(2022, 5, 1, 8, 15, 5, 674000000, ZoneOffset.of("+01:00"))
        val starttimestring  = starttime.format(DateTimeFormatter.ISO_DATE_TIME)
        val endtime = OffsetDateTime.of(2022, 5, 2, 8, 15, 5, 674000000, ZoneOffset.of("+01:00"))
        val endtimestring  = endtime.format(DateTimeFormatter.ISO_DATE_TIME)

        // when
        clientAndServer
            .`when`(
                HttpRequest.request()
                    .withMethod("GET")
                    .withPath("/fdsnws/event/1/query")
                    .withQueryStringParameters(
                        Parameter.param("format", "geojson"),
                        Parameter.param("limit", "100"),
                        Parameter.param("minmagnitude", "5.0"),
                        Parameter.param("maxmagnitude", "10.0"),
                        Parameter.param("starttime", starttimestring),
                        Parameter.param("endtime", endtimestring),
                        Parameter.param("orderby", "time-asc")
                    )
            )
            .respond(
                HttpResponse.response()
                    .withStatusCode(404)
            )

        assertThrows<Exception> {
            underTest.getTopEarthquakeForToday(
                "geojson", 100, 5.0, 10.0,
                starttimestring, endtimestring, "time-asc"
            ).first()
        }.printStackTrace()

        // then
    }


    test("GET earthquake return 500 should throw exception") {

        clientAndServer.reset()

        // given
        val starttime = OffsetDateTime.of(2022, 5, 1, 8, 15, 5, 674000000, ZoneOffset.of("+01:00"))
        val starttimestring  = starttime.format(DateTimeFormatter.ISO_DATE_TIME)
        val endtime = OffsetDateTime.of(2022, 5, 2, 8, 15, 5, 674000000, ZoneOffset.of("+01:00"))
        val endtimestring  = endtime.format(DateTimeFormatter.ISO_DATE_TIME)

        // when
        clientAndServer
            .`when`(
                HttpRequest.request()
                    .withMethod("GET")
                    .withPath("/fdsnws/event/1/query")
                    .withQueryStringParameters(
                        Parameter.param("format", "geojson"),
                        Parameter.param("limit", "100"),
                        Parameter.param("minmagnitude", "5.0"),
                        Parameter.param("maxmagnitude", "10.0"),
                        Parameter.param("starttime", starttimestring),
                        Parameter.param("endtime", endtimestring),
                        Parameter.param("orderby", "time-asc")
                    )
            )
            .respond(
                HttpResponse.response()
                    .withStatusCode(500)
            )

        assertThrows<Exception> {
            underTest.getTopEarthquakeForToday(
                "geojson", 100, 5.0, 10.0,
                starttimestring, endtimestring, "time-asc"
            ).first()
        }.printStackTrace()

        // then
    }
})
