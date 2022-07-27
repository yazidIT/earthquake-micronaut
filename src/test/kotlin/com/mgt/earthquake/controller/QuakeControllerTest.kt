package com.mgt.earthquake.controller

import com.mgt.earthquake.jooqmodel.tables.records.QuakeRecord
import com.mgt.earthquake.model.*
import com.mgt.earthquake.service.QuakeService
import com.mgt.earthquake.service.QuakeServiceImpl
import com.mgt.earthquake.service.QuakeSqlService
import com.mgt.earthquake.service.QuakeSqlServiceImpl
import io.kotest.core.spec.style.FunSpec
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.bson.types.ObjectId
import org.junit.jupiter.api.Assertions
import org.slf4j.LoggerFactory

@MicronautTest
class QuakeControllerTest(

    @Client("/")
    val httpClient: HttpClient,

    val quakeService: QuakeService,
    val quakeSqlService: QuakeSqlService

) : FunSpec({

    val logger = LoggerFactory.getLogger(QuakeControllerTest::class.java)

    test("POST /quake/add should complete successfully") {

        // given
        val quakedto = QuakeDTO(title = "Quake No 1", magnitude = 6.0, quaketime = "xxx",
            latitude = 3.1234, longitude = 103.3, quakeid = "fwiohfrwier1")
        val quake = QuakeModel(id= ObjectId(), title = "Quake No 1", magnitude = 6.0, quaketime = "xxx",
            latitude = 3.1234, longitude = 103.3, quakeid = "fwiohfrwier1")
        val quakesql = QuakeRecord(id = 1234L, title = "Quake No 1", magnitude = 6.0, quaketime = "xxx",
            latitude = 3.1234, longitude = 103.3, quakeid = "fwiohfrwier1")

        // when
        coEvery { quakeService.create(any()) } returns quake
        coEvery { quakeSqlService.create(any()) } returns quakesql

        val request = HttpRequest.POST<Any>("/quake/add", quakedto)

        val httpresponse = httpClient.toBlocking().exchange(request, QuakeModel::class.java)

        // then
        Assertions.assertEquals(HttpStatus.OK, httpresponse.status)
        Assertions.assertTrue(httpresponse.body.isPresent)

        val data = httpresponse.body.get()
        logger.info("$data")
    }

    test("POST /quake/addlist should complete successfully") {

        // given
        val quakedto = QuakeDTO(title = "Quake No 1", magnitude = 6.0, quaketime = "xxx",
            latitude = 3.1234, longitude = 103.3, quakeid = "fwiohfrwier1")
        val quakelist = QuakeDTOList(listOf(quakedto))
        val quake = QuakeModel(id= ObjectId(), title = "Quake No 1", magnitude = 6.0, quaketime = "xxx",
            latitude = 3.1234, longitude = 103.3, quakeid = "fwiohfrwier1")
        val quake2 = QuakeModel(id= ObjectId(), title = "Quake No 2", magnitude = 6.5, quaketime = "xxxss",
            latitude = 3.1234, longitude = 103.3, quakeid = "fwiohfdd1")

        // when
        coEvery { quakeService.createList(any()) } returns listOf(quake, quake2)
        coEvery { quakeSqlService.createList(any()) } just Runs

        val request = HttpRequest.POST<Any>("/quake/addlist", quakelist)

        val httpresponse = httpClient.toBlocking().exchange(request, List::class.java)

        // then
        Assertions.assertEquals(HttpStatus.OK, httpresponse.status)
        Assertions.assertTrue(httpresponse.body.isPresent)

        val data = httpresponse.body.get()
        logger.info("$data")
    }

    test("GET /quake/latest should complete successfully") {

        // given
        val quakeproperty = QuakeProperty(time = 123456, title = "This is sample quake test", mag = 9.5)
        val quakegeo = QuakeGeometry(coordinates = arrayListOf(5.5, 6.6, 7.7))
        val quakeresponsefeature = QuakeResponseFeature(id = "1234", properties = quakeproperty, geometry = quakegeo)
        val quakeresponse = QuakeResponse(listOf(quakeresponsefeature))

        // when
        coEvery { quakeService.latestQuake() } returns flowOf(quakeresponse)

        val request = HttpRequest.GET<String>("/quake/latest")

        val httpresponse = httpClient.toBlocking().exchange(request, List::class.java)

        // then
        Assertions.assertEquals(HttpStatus.OK, httpresponse.status)
        Assertions.assertTrue(httpresponse.body.isPresent)

        val data = httpresponse.body.get()
        logger.info("$data")
    }

}) {

    @MockBean(QuakeServiceImpl::class)
    fun quakeService(): QuakeServiceImpl = mockk()

    @MockBean(QuakeSqlServiceImpl::class)
    fun quakeSqlService(): QuakeSqlServiceImpl = mockk()
}
