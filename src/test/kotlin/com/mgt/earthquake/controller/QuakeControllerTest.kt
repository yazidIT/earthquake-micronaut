package com.mgt.earthquake.controller

import com.mgt.earthquake.model.QuakeDTO
import com.mgt.earthquake.model.QuakeDTOList
import com.mgt.earthquake.model.QuakeModel
import com.mgt.earthquake.service.QuakeService
import com.mgt.earthquake.service.QuakeServiceImpl
import io.kotest.core.spec.style.FunSpec
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import io.mockk.coEvery
import io.mockk.mockk
import org.bson.types.ObjectId
import org.junit.jupiter.api.Assertions
import org.slf4j.LoggerFactory

@MicronautTest
class QuakeControllerTest(

    @Client("/")
    val httpClient: HttpClient,

    val quakeService: QuakeService

) : FunSpec({

    val logger = LoggerFactory.getLogger(QuakeControllerTest::class.java)

    test("POST /quake/add should complete successfully") {

        // given
        val quakedto = QuakeDTO(title = "Quake No 1", magnitude = 6.0, quaketime = "xxx",
            latitude = 3.1234, longitude = 103.3, quakeid = "fwiohfrwier1")
        val quake = QuakeModel(id= ObjectId(), title = "Quake No 1", magnitude = 6.0, quaketime = "xxx",
            latitude = 3.1234, longitude = 103.3, quakeid = "fwiohfrwier1")

        // when
        coEvery { quakeService.create(any()) } returns quake

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

        val request = HttpRequest.POST<Any>("/quake/addlist", quakelist)

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
}
