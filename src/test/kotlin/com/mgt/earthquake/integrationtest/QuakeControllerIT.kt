package com.mgt.earthquake.integrationtest

import com.mgt.earthquake.dao.QuakeRepository
import com.mgt.earthquake.dao.QuakeSqlRepository
import com.mgt.earthquake.model.QuakeDTO
import com.mgt.earthquake.model.QuakeDTOList
import com.mgt.earthquake.model.QuakeModel
import com.mgt.earthquake.model.QuakeResponse
import com.mgt.earthquake.service.QuakeService
import com.mgt.earthquake.service.QuakeSqlService
import io.kotest.core.spec.style.FunSpec
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.StreamingHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.kotest5.annotation.MicronautTest
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import org.junit.jupiter.api.Assertions
import org.slf4j.LoggerFactory

@MicronautTest(transactional = false, environments = ["integrationtest"])
class QuakeControllerIT(

    @Client("/")
    val httpClient: HttpClient,
    @Client("/")
    val streamClient: StreamingHttpClient,

    val quakeSqlRepo: QuakeSqlRepository,
    val quakeRepo: QuakeRepository,
    val quakeSqlService: QuakeSqlService,
    val quakeService: QuakeService

) : FunSpec({

    val logger = LoggerFactory.getLogger(QuakeControllerIT::class.java)

    beforeSpec {
        val quake1 = QuakeModel(title = "Quake NE Japan1", magnitude = 6.5, latitude = 3.1414,
            longitude = 103.4534, quaketime = "2022-04-22T06:15:23.756000", quakeid = "us6000hfxm")
        val quake2 = QuakeModel(title = "Quake NE Japan2", magnitude = 6.9, latitude = 11.73455,
            longitude = 90.232323, quaketime = "2022-05-22T06:15:23.756000", quakeid = "us6000hfjk")
        val quake3 = QuakeModel(title = "Quake NE Japan3", magnitude = 6.5, latitude = 3.7788,
            longitude = 67.4534, quaketime = "2022-04-22T06:15:23.756000", quakeid = "us6000hfxn")
        val quake4 = QuakeModel(title = "Quake NE Japan4", magnitude = 6.9, latitude = 5.73455,
            longitude = 72.232323, quaketime = "2022-05-22T06:15:23.756000", quakeid = "us6000hfjl")

        quakeRepo.saveAll(listOf(quake1, quake2, quake3, quake4)).toList()

    }
    afterSpec {
        quakeRepo.deleteAll()
        quakeSqlRepo.deleteAll()
    }

    test("GET /quake/latest should complete successfully") {

        val request = HttpRequest.GET<String>("/quake/latest")

        var count = 0
        streamClient.jsonStream(request, Argument.of(QuakeResponse::class.java)).asFlow()
            .collect {
                logger.info("$it")
                count += 1
            }

        // then
        Assertions.assertTrue(count > 0)
    }

    test("quakeSqlService create should work") {
        // given
        val quakedto = QuakeDTO(
            title = "Quake NE Japan", magnitude = 6.5, latitude = 3.1414,
            longitude = 103.4534, quaketime = "2022-04-22T06:15:23.756000", quakeid = "us6000hfxa"
        )

        val result = quakeSqlService.create(quakedto)

        // then
        Assertions.assertNotNull(result)
        logger.info("$result")
    }

    test("quakeService create should work") {
        // given
        val quakedto = QuakeDTO(
            title = "Quake NE Japan", magnitude = 6.5, latitude = 3.1414,
            longitude = 103.4534, quaketime = "2022-04-22T06:15:23.756000", quakeid = "us6000nbhj"
        )

        val result = quakeService.create(quakedto)

        // then
        Assertions.assertNotNull(result)
        logger.info("$result")
    }

    test("POST /quake/add should complete successfully") {

        // given
        val quakedto = QuakeDTO(
            title = "Quake NE Japan", magnitude = 6.5, latitude = 3.1414,
            longitude = 103.4534, quaketime = "2022-04-22T06:15:23.756000", quakeid = "us6000asdf"
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

        Assertions.assertEquals(6, result.size)
        val check = result.filter { it.quakeid == quakedto.quakeid }
        Assertions.assertTrue(check.isNotEmpty())

        // Read data from mysql
        val resultsql = quakeSqlRepo.findAll()

        Assertions.assertEquals(2, resultsql.size)

        val checksql = resultsql.filter { it.quakeid == "us6000asdf" }
        Assertions.assertTrue(checksql.isNotEmpty())
    }

    test("quakeService createList should work") {
        // given
        val quakedto = QuakeDTO(
            title = "Quake NE Japan", magnitude = 6.5, latitude = 3.1414,
            longitude = 103.4534, quaketime = "2022-04-22T06:15:23.756000", quakeid = "us6000nbhj"
        )
        val quakedto2 = QuakeDTO(
            title = "Quake NE Japan", magnitude = 6.9, latitude = 11.982,
            longitude = 78.4534, quaketime = "2022-04-22T06:15:23.756000", quakeid = "us6000kler"
        )
        val quakelist = QuakeDTOList(listOf(quakedto, quakedto2))

        val result = quakeService.createList(quakelist.quakeList).toList()

        // then
        Assertions.assertNotNull(result)
        logger.info("$result")
    }

    test("POST /quake/addlist should complete successfully") {

        // given
        val quakedto = QuakeDTO(
            title = "Quake NE Japan", magnitude = 8.5, latitude = 8.1414,
            longitude = 88.4534, quaketime = "2022-04-22T06:15:23.756000", quakeid = "us6000klmn"
        )
        val quakedto2 = QuakeDTO(
            title = "Quake NE Japan", magnitude = 6.2, latitude = 6.982,
            longitude = 65.4534, quaketime = "2022-04-22T06:15:23.756000", quakeid = "us6000klmm"
        )
        val quakelist = QuakeDTOList(listOf(quakedto, quakedto2))

        val request = HttpRequest.POST<Any>("/quake/addlist", quakelist)

        val httpresponse = httpClient.toBlocking().exchange(request, Argument.listOf(QuakeModel::class.java))

        // then
        Assertions.assertEquals(HttpStatus.OK, httpresponse.status)
        Assertions.assertTrue(httpresponse.body.isPresent)

        val data = httpresponse.body.get()
        logger.info("$data")
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

        // then
        var count = 0
        streamClient.jsonStream(request, Argument.of(QuakeModel::class.java)).asFlow()
            .collect {
                logger.info("$it")
                count += 1
            }

        // then
        Assertions.assertEquals(3, count)

    }
})