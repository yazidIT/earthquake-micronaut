package com.mgt.earthquake.exception

import com.mgt.earthquake.jooqmodel.tables.records.QuakeRecord
import com.mgt.earthquake.model.QuakeDTO
import com.mgt.earthquake.service.QuakeService
import com.mgt.earthquake.service.QuakeServiceImpl
import com.mgt.earthquake.service.QuakeSqlService
import com.mgt.earthquake.service.QuakeSqlServiceImpl
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.hateoas.JsonError
import io.micronaut.serde.ObjectMapper
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.kotest5.MicronautKotest5Extension.getMock
import io.micronaut.test.extensions.kotest5.annotation.MicronautTest
import io.mockk.coEvery
import io.mockk.mockk
import org.slf4j.LoggerFactory
import java.rmi.ConnectIOException

@MicronautTest(transactional = false)
class ExceptionControllerTest (

    @Client("/")
    val client: HttpClient,

    val quakeService: QuakeService,
    val quakeSqlService: QuakeSqlService,

    val objectMapper: ObjectMapper

) : FunSpec({

    val logger = LoggerFactory.getLogger(this::class.java)

    test("test RunTime Exception will be handled by GlobalExceptionHandler") {

        getMock(quakeService)
        getMock(quakeSqlService)
        // given
        val quakedto = QuakeDTO(title = "Quake No 1", magnitude = 6.0, quaketime = "xxx",
            latitude = 3.1234, longitude = 103.3, quakeid = "fwiohfrwier1")
        val quakesql = QuakeRecord(id = 1234L, title = "Quake No 1", magnitude = 6.0, quaketime = "xxx",
            latitude = 3.1234, longitude = 103.3, quakeid = "fwiohfrwier1")

        // when
        coEvery { quakeService.create(any()) } throws (RuntimeException("Mock Runtime Exception"))
        coEvery { quakeSqlService.create(any()) } returns quakesql

        val request = HttpRequest.POST<Any>("/quake/add", quakedto)
        val httpresponse = client.toBlocking().exchange(request,
            Argument.ofTypeVariable(ResponseError::class.java, null, null,
                Argument.of(JsonError::class.java)))

        // then
        httpresponse.status shouldBe HttpStatus.OK

        val jsonerrorstring = objectMapper.writeValueAsString(httpresponse.body().error!!)
        val jsonerror = objectMapper.readValue(jsonerrorstring, JsonError::class.java)
        jsonerror!!.message shouldBe "Mock Runtime Exception"
    }

    test("test IO Exception will be handled by GlobalExceptionHandler") {

        getMock(quakeService)
        getMock(quakeSqlService)
        // given
        val quakedto = QuakeDTO(title = "Quake No 1", magnitude = 6.0, quaketime = "xxx",
            latitude = 3.1234, longitude = 103.3, quakeid = "fwiohfrwier1")
        val quakesql = QuakeRecord(id = 1234L, title = "Quake No 1", magnitude = 6.0, quaketime = "xxx",
            latitude = 3.1234, longitude = 103.3, quakeid = "fwiohfrwier1")

        // when
        coEvery { quakeService.create(any()) } throws (ConnectIOException("Mock IO Exception"))
        coEvery { quakeSqlService.create(any()) } returns quakesql

        val request = HttpRequest.POST<Any>("/quake/add", quakedto)
        val httpresponse = client.toBlocking().exchange(request,
            Argument.ofTypeVariable(ResponseError::class.java, null, null,
                Argument.of(JsonError::class.java)))

        // then
        httpresponse.status shouldBe HttpStatus.OK

        val jsonerrorstring = objectMapper.writeValueAsString(httpresponse.body().error!!)
        val jsonerror = objectMapper.readValue(jsonerrorstring, JsonError::class.java)
        jsonerror!!.message shouldBe "Mock IO Exception"
    }

}) {

    @MockBean(QuakeServiceImpl::class)
    fun quakeService(): QuakeServiceImpl = mockk()

    @MockBean(QuakeSqlServiceImpl::class)
    fun quakeSqlService(): QuakeSqlServiceImpl = mockk()
}

