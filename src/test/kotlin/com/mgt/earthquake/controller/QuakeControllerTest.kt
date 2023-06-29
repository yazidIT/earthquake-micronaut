package com.mgt.earthquake.controller

import com.mgt.earthquake.jooqmodel.tables.records.QuakeRecord
import com.mgt.earthquake.model.*
import com.mgt.earthquake.service.QuakeService
import com.mgt.earthquake.service.QuakeServiceImpl
import com.mgt.earthquake.service.QuakeSqlService
import com.mgt.earthquake.service.QuakeSqlServiceImpl
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.StreamingHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.kotest5.MicronautKotest5Extension.getMock
import io.micronaut.test.extensions.kotest5.annotation.MicronautTest
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.reactive.asFlow
import org.bson.types.ObjectId
import org.junit.jupiter.api.Assertions
import org.slf4j.LoggerFactory

@MicronautTest(transactional = false)
class QuakeControllerTest(

    @Client("/")
    val httpClient: HttpClient,

    @Client("/")
    val streamClient: StreamingHttpClient,

    val quakeService: QuakeService,
    val quakeSqlService: QuakeSqlService,

) : FunSpec({

    val logger = LoggerFactory.getLogger(QuakeControllerTest::class.java)

    test("POST /quake/add should complete successfully") {

        // given
        val quakedto = QuakeDTO(title = "Quake No 1", magnitude = 6.0, quaketime = "xxx",
            latitude = 3.1234, longitude = 103.3, quakeid = "fwiohfrwier1")
        val quake = QuakeModel(id = ObjectId(), title = "Quake No 1", magnitude = 6.0, quaketime = "xxx",
            latitude = 3.1234, longitude = 103.3, quakeid = "fwiohfrwier1")
        val quakesql = QuakeRecord(id = 1234L, title = "Quake No 1", magnitude = 6.0, quaketime = "xxx",
            latitude = 3.1234, longitude = 103.3, quakeid = "fwiohfrwier1")

        // when
        coEvery { quakeService.create(any()) } returns quake
        coEvery { quakeSqlService.create(any()) } returns quakesql

        val request = HttpRequest.POST<Any>("/quake/add", quakedto)

        val httpresponse = httpClient.toBlocking().exchange(request, Argument.of(QuakeModel::class.java))

        // then
        Assertions.assertEquals(HttpStatus.OK, httpresponse.status)
        Assertions.assertTrue(httpresponse.body.isPresent)

        val data = httpresponse.body.get()

        logger.info("$data")
        data.title shouldBe quake.title
    }

    test("POST /quake/add should throw - quakeService throws Exception") {

        // given
        val quakedto = QuakeDTO(title = "Quake No 1", magnitude = 6.0, quaketime = "xxx",
            latitude = 3.1234, longitude = 103.3, quakeid = "fwiohfrwier1")
        val quake = QuakeModel(title = "Quake No 1", magnitude = 6.0, quaketime = "xxx",
            latitude = 3.1234, longitude = 103.3, quakeid = "fwiohfrwier1")
        val quakesql = QuakeRecord(id = 1234L, title = "Quake No 1", magnitude = 6.0, quaketime = "xxx",
            latitude = 3.1234, longitude = 103.3, quakeid = "fwiohfrwier1")

        // when
        coEvery { quakeService.create(any()) } throws Exception()
        coEvery { quakeSqlService.create(any()) } returns quakesql

        val request = HttpRequest.POST<Any>("/quake/add", quakedto)

        shouldThrow<Exception> {
            httpClient.toBlocking().exchange(request, Argument.of(QuakeModel::class.java))
        }.printStackTrace()

        // then
    }

    test("POST /quake/addlist should complete successfully") {

        // given
        val quakedto = QuakeDTO(title = "Quake No 1", magnitude = 6.0, quaketime = "xxx",
            latitude = 3.1234, longitude = 103.3, quakeid = "fwiohfrwier1")
        val quakelist = QuakeDTOList(listOf(quakedto))
        val quake = QuakeModel(title = "Quake No 1", magnitude = 6.0, quaketime = "xxx",
            latitude = 3.1234, longitude = 103.3, quakeid = "fwiohfrwier1")
        val quake2 = QuakeModel(title = "Quake No 2", magnitude = 6.5, quaketime = "xxxss",
            latitude = 3.1234, longitude = 103.3, quakeid = "fwiohfdd1")

        // when
        coEvery { quakeSqlService.createList(any()) } just Runs
        coEvery { quakeService.createList(any()) } returns flowOf(quake, quake2)

        val request = HttpRequest.POST<Any>("/quake/addlist", quakelist)

        val httpresponse = httpClient.toBlocking().exchange(request, Argument.listOf(QuakeModel::class.java))

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
        val quakeproperty2 = QuakeProperty(time = 123478, title = "This is sample quake test2", mag = 7.5)
        val quakegeo2 = QuakeGeometry(coordinates = arrayListOf(4.5, 7.6, 8.7))
        val quakeresponsefeature2 = QuakeResponseFeature(id = "1235", properties = quakeproperty2, geometry = quakegeo2)
        val quakeresponse = QuakeResponse(listOf(quakeresponsefeature, quakeresponsefeature2))

        // when
        coEvery { quakeService.latestQuake() } returns flowOf(quakeresponse)

        val request = HttpRequest.GET<String>("/quake/latest")

        val httpresponse = httpClient.toBlocking().exchange(request, Argument.listOf(QuakeResponse::class.java))

        // then
        Assertions.assertEquals(HttpStatus.OK, httpresponse.status)
        Assertions.assertTrue(httpresponse.body.isPresent)

        val data = httpresponse.body.get()
        logger.info("$data")
    }

    test("GET /quake/latest should complete successfully - empty list return from earthquake portal") {

        // given

        // when
        coEvery { quakeService.latestQuake() } returns flowOf(QuakeResponse(features = emptyList()))

        val request = HttpRequest.GET<String>("/quake/latest")

        val httpresponse = httpClient.toBlocking().exchange(request, Argument.listOf(QuakeResponse::class.java))

        // then
        Assertions.assertEquals(HttpStatus.OK, httpresponse.status)
        Assertions.assertTrue(httpresponse.body.isPresent)

        val data = httpresponse.body.get()
        logger.info("$data")
    }

    test("GET /quake/list/json/{number} should complete successfully") {

        getMock(quakeService)
        // given
        val quake1 = QuakeModel(title = "Quake NE Japan1", magnitude = 6.5, latitude = 3.1414,
            longitude = 103.4534, quaketime = "2022-04-22T06:15:23.756000", quakeid = "us6000hfxm")
        val quake2 = QuakeModel(title = "Quake NE Japan2", magnitude = 6.9, latitude = 5.73455,
            longitude = 90.232323, quaketime = "2022-05-22T06:15:23.756000", quakeid = "us6000hfjk")
        val quake3 = QuakeModel(title = "Quake NE Japan3", magnitude = 6.5, latitude = 3.1414,
            longitude = 103.4534, quaketime = "2022-04-22T06:15:23.756000", quakeid = "us6000hfxn")
        val quake4 = QuakeModel(title = "Quake NE Japan4", magnitude = 6.9, latitude = 5.73455,
            longitude = 90.232323, quaketime = "2022-05-22T06:15:23.756000", quakeid = "us6000hfjl")
        val number = 4

        // when
        coEvery { quakeService.latestNumberOfQuake(any()) } returns flowOf(quake1, quake2, quake3, quake4)

        val request = HttpRequest.GET<String>("/quake/list/json/$number")

        var count = 0
        streamClient.jsonStream(request, Argument.of(QuakeModel::class.java)).asFlow()
            .collect {
                logger.info("$it")
                count += 1
            }

        // then
        Assertions.assertEquals(4, count)
    }


    test("GET /quake/list/{number} should complete successfully") {

        getMock(quakeService)
        // given
        val quake1 = QuakeModel(title = "Quake NE Japan1", magnitude = 6.5, latitude = 3.1414,
            longitude = 103.4534, quaketime = "2022-04-22T06:15:23.756000", quakeid = "us6000hfxm")
        val quake2 = QuakeModel(title = "Quake NE Japan2", magnitude = 6.9, latitude = 5.73455,
            longitude = 90.232323, quaketime = "2022-05-22T06:15:23.756000", quakeid = "us6000hfjk")
        val quake3 = QuakeModel(title = "Quake NE Japan3", magnitude = 6.5, latitude = 3.1414,
            longitude = 103.4534, quaketime = "2022-04-22T06:15:23.756000", quakeid = "us6000hfxn")
        val quake4 = QuakeModel(title = "Quake NE Japan4", magnitude = 6.9, latitude = 5.73455,
            longitude = 90.232323, quaketime = "2022-05-22T06:15:23.756000", quakeid = "us6000hfjl")
        val number = 4

        // when
        coEvery { quakeService.latestNumberOfQuake(any()) } returns flowOf(quake1, quake2, quake3, quake4)

        val request = HttpRequest.GET<String>("/quake/list/$number")

        var count = 0
        streamClient.jsonStream(request, Argument.of(QuakeModel::class.java)).asFlow()
            .collect {
                logger.info("$it")
                count += 1
            }

        // then
        Assertions.assertEquals(4, count)

    }

}) {

    @MockBean(QuakeServiceImpl::class)
    fun quakeService(): QuakeServiceImpl = mockk()

    @MockBean(QuakeSqlServiceImpl::class)
    fun quakeSqlService(): QuakeSqlServiceImpl = mockk()
}
