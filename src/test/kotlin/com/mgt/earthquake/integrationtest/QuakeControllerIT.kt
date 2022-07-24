package com.mgt.earthquake.integrationtest

import io.kotest.core.spec.style.FunSpec
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.slf4j.LoggerFactory

@MicronautTest
class QuakeControllerIT(

    @Client("/")
    val httpClient: HttpClient,

) : FunSpec({

    val logger = LoggerFactory.getLogger(QuakeControllerIT::class.java)

    test("GET /quake/latest should complete successfully") {

        val request = HttpRequest.GET<String>("/quake/latest")

        val httpresponse = httpClient.toBlocking().exchange(request, List::class.java)

        // then
        Assertions.assertEquals(HttpStatus.OK, httpresponse.status)
        Assertions.assertTrue(httpresponse.body.isPresent)

        val data = httpresponse.body.get()
        logger.info("$data")
    }

})