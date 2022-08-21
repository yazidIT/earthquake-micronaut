package com.mgt.earthquake.integrationtest

import com.mgt.earthquake.dao.QuakeRepository
import com.mgt.earthquake.dao.QuakeSqlRepository
import com.mgt.earthquake.model.QuakeDTO
import com.mgt.earthquake.model.QuakeModel
import io.kotest.core.spec.style.FunSpec
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.StreamingHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import org.junit.jupiter.api.Assertions
import org.slf4j.LoggerFactory

@MicronautTest(transactional = true, environments = ["integrationtest"])
class QuakeControllerIT(

    @Client("/")
    val httpClient: HttpClient,
    @Client("/")
    val streamClient: StreamingHttpClient,

    val quakeSqlRepo: QuakeSqlRepository,
    val quakeRepo: QuakeRepository

) : FunSpec({

    val logger = LoggerFactory.getLogger(QuakeControllerIT::class.java)

    beforeSpec {
        val quake1 = QuakeModel(title = "Quake NE Japan1", magnitude = 6.5, latitude = 3.1414,
            longitude = 103.4534, quaketime = "2022-04-22T06:15:23.756000", quakeid = "us6000hfxm")
        val quake2 = QuakeModel(title = "Quake NE Japan2", magnitude = 6.9, latitude = 5.73455,
            longitude = 90.232323, quaketime = "2022-05-22T06:15:23.756000", quakeid = "us6000hfjk")
        val quake3 = QuakeModel(title = "Quake NE Japan3", magnitude = 6.5, latitude = 3.1414,
            longitude = 103.4534, quaketime = "2022-04-22T06:15:23.756000", quakeid = "us6000hfxn")
        val quake4 = QuakeModel(title = "Quake NE Japan4", magnitude = 6.9, latitude = 5.73455,
            longitude = 90.232323, quaketime = "2022-05-22T06:15:23.756000", quakeid = "us6000hfjl")

        quakeRepo.saveAll(listOf(quake1, quake2, quake3, quake4)).toList()

    }
    afterSpec {
        quakeRepo.deleteAll()
        quakeSqlRepo.deleteAll()
    }

    test("GET /quake/latest should complete successfully") {

        val request = HttpRequest.GET<String>("/quake/latest")

        val httpresponse: HttpResponse<List<*>> = httpClient.toBlocking().exchange(request, Argument.of(List::class.java))

        // then
        Assertions.assertEquals(HttpStatus.OK, httpresponse.status)
        Assertions.assertTrue(httpresponse.body.isPresent)

        val data = httpresponse.body.get()
        logger.info("$data")
    }

    test("POST /quake/add should complete successfully") {

        // given
        val quakedto = QuakeDTO(
            title = "Quake NE Japan", magnitude = 6.5, latitude = 3.1414,
            longitude = 103.4534, quaketime = "2022-04-22T06:15:23.756000", quakeid = "us6000hfxm"
        )

        val request = HttpRequest.POST("/quake/add", quakedto)

        val httpresponse: HttpResponse<QuakeModel> = httpClient.toBlocking().exchange(request, Argument.of(QuakeModel::class.java))

        // then
        Assertions.assertEquals(HttpStatus.OK, httpresponse.status)
        Assertions.assertTrue(httpresponse.body.isPresent)

        val responsedata = httpresponse.body()!!
        logger.info("$responsedata")

        // Read data from mongodb
        val result = quakeRepo.findAll().toList()

        Assertions.assertEquals(5, result.size)
        Assertions.assertEquals( "us6000hfxm", result[0].quakeid)

        // Read data from mysql
        val resultsql = quakeSqlRepo.findAll()

        Assertions.assertEquals(1, resultsql.size)
        Assertions.assertEquals(103.4534, resultsql[0].longitude)
    }

    test("GET /quake/list/json/{number} should complete successfully") {

        // given
        val number = 3

        val request = HttpRequest.GET<String>("/quake/list/json/$number")

        var count = 0
        streamClient.jsonStream(request, Argument.of(QuakeModel::class.java)).asFlow()
            .collect {
                logger.info("$it")
                count += 1
            }

        // then
        Assertions.assertEquals(3, count)
    }


    test("GET /quake/list/{number} should complete successfully") {

        // given
        val number = 3

        val request = HttpRequest.GET<String>("/quake/list/$number")
        val httpresponse = httpClient.toBlocking().exchange(request, Argument.listOf(QuakeModel::class.java))

        // then
        Assertions.assertEquals(3, httpresponse.body()!!.size)
        logger.info("${httpresponse.body()}")
    }
})