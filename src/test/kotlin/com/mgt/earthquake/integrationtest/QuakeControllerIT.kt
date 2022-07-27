package com.mgt.earthquake.integrationtest

import com.mgt.earthquake.dao.QuakeRepository
import com.mgt.earthquake.dao.QuakeSqlRepository
import com.mgt.earthquake.model.QuakeDTO
import com.mgt.earthquake.model.QuakeModel
import io.kotest.core.spec.style.FunSpec
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import kotlinx.coroutines.flow.toList
import org.junit.jupiter.api.Assertions
import org.slf4j.LoggerFactory

@MicronautTest(transactional = true, environments = ["integrationtest"])
class QuakeControllerIT(

    @Client("/")
    val httpClient: HttpClient,

    val quakeSqlRepo: QuakeSqlRepository,
    val quakeRepo: QuakeRepository

) : FunSpec({

    val logger = LoggerFactory.getLogger(QuakeControllerIT::class.java)

    afterSpec {
        quakeRepo.deleteAll()
        quakeSqlRepo.deleteAll()
    }

    test("GET /quake/latest should complete successfully") {

        val request = HttpRequest.GET<String>("/quake/latest")

        val httpresponse = httpClient.toBlocking().exchange(request, List::class.java)

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

        val httpresponse = httpClient.toBlocking().exchange(request, Argument.of(QuakeModel::class.java))

        // then
        Assertions.assertEquals(HttpStatus.OK, httpresponse.status)
        Assertions.assertTrue(httpresponse.body.isPresent)

        // Read data from mongodb
        val result = quakeRepo.findAll().toList()

        Assertions.assertEquals(1, result.size)
        Assertions.assertEquals( "us6000hfxm", result[0].quakeid)

        // Read data from mysql
        val resultsql = quakeSqlRepo.findAll()

        Assertions.assertEquals(1, resultsql.size)
        Assertions.assertEquals(103.4534, resultsql[0].longitude)
    }
})