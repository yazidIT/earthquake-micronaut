package com.mgt.earthquake.exception

import com.mgt.earthquake.model.QuakeDTO
import com.mgt.earthquake.model.QuakeModel
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.micronaut.core.type.Argument
import io.micronaut.serde.ObjectMapper
import io.micronaut.serde.annotation.SerdeImport
import org.slf4j.LoggerFactory

@SerdeImport(RuntimeException::class)
class ResponseErrorTest : FunSpec({

    val logger = LoggerFactory.getLogger(this::class.java)

    val objectMapper = ObjectMapper.getDefault()

    test("serdes with code only should be OK") {

        // given
        val responseerror = ResponseError(500, 0)

        val serResult = objectMapper.writeValueAsString(responseerror)
        val desResult = objectMapper.readValue(serResult, Argument.ofTypeVariable(ResponseError::class.java,
            null, null, Argument.INT))

        // then
        desResult shouldNotBe null
        desResult.code shouldBe responseerror.code
    }

    test("serdes with error only should be OK") {

        // given
        val responseerror = ResponseError(500, 400001)

        val serResult = objectMapper.writeValueAsString(responseerror)
        val desResult = objectMapper.readValue(serResult, Argument.ofTypeVariable(ResponseError::class.java,
            null, null, Argument.INT))

        // then
        desResult shouldNotBe null
        desResult.code shouldBe responseerror.code

        val errordata = desResult.error as Int
        errordata shouldBe responseerror.error
    }

    test("serdes with error object only should be OK") {

        // given
        val quake1 = QuakeDTO(title = "Quake NE Japan", magnitude = 6.5, latitude = 3.1414,
            longitude = 103.4534, quaketime = "2022-04-22T06:15:23.756000", quakeid = "us6000hfxm")
        val responseerror = ResponseError(500, quake1)

        val serResult = objectMapper.writeValueAsString(responseerror)
        val desResult = objectMapper.readValue(serResult, Argument.ofTypeVariable(ResponseError::class.java,
            null, null, Argument.of(QuakeDTO::class.java)))

        // then
        desResult shouldNotBe null
        desResult!!.code shouldBe responseerror.code

        val errordata = objectMapper.readValue(objectMapper.writeValueAsString(desResult.error),
            Argument.of(QuakeDTO::class.java))
        errordata.title shouldBe quake1.title
    }

    test("serdes with error object and stacktrace should be OK") {

        // given
        val quake1 = QuakeDTO(title = "Quake NE Japan", magnitude = 6.5, latitude = 3.1414,
            longitude = 103.4534, quaketime = "2022-04-22T06:15:23.756000", quakeid = "us6000hfxm")
        val responseerror = ResponseError(500, quake1).apply {
            stacktrace = "Stacktrace Sample"
        }

        val serResult = objectMapper.writeValueAsString(responseerror)
        val desResult = objectMapper.readValue(serResult, Argument.ofTypeVariable(ResponseError::class.java,
            null, null, Argument.of(QuakeDTO::class.java)))

        // then
        desResult shouldNotBe null
        desResult!!.code shouldBe responseerror.code
        desResult.stacktrace shouldBe responseerror.stacktrace

        val errordata = objectMapper.readValue(objectMapper.writeValueAsString(desResult.error),
            Argument.of(QuakeDTO::class.java))
        errordata.title shouldBe quake1.title
    }

    xtest("serdes with exception error should be OK") {

        // given
        val exception = RuntimeException("Exception ResponseError Test")
        val responseerror = ResponseError(500, exception)

        val serResult = objectMapper.writeValueAsString(responseerror)
        val desResult = objectMapper.readValue(serResult, Argument.ofTypeVariable(ResponseError::class.java,
            null, null, Argument.of(RuntimeException::class.java)))

        // then
        desResult shouldNotBe null
        desResult.code shouldBe responseerror.code

        println(objectMapper.writeValueAsString(desResult.error!!))

        val errordata = objectMapper.readValue(objectMapper.writeValueAsString(desResult.error!!),
                            Argument.of(RuntimeException::class.java))

        errordata shouldNotBe null
        errordata!!.message shouldBe responseerror.error.message
    }
})

inline fun <reified T> convertJsonToCustomPojo(jsonObject: Any?): T? {
    val objectMapper: ObjectMapper = ObjectMapper.getDefault()

    return runCatching {
        val jsonString = objectMapper.writeValueAsString(jsonObject!!)
        objectMapper.readValue(jsonString, Argument.of(T::class.java))
    }.getOrElse {
        it.printStackTrace()
        null
    }
}

inline fun <reified T> convertCustomPojoToJson(pojoObject: T): String? {
    val objectMapper: ObjectMapper = ObjectMapper.getDefault()

    return runCatching {
        objectMapper.writeValueAsString(pojoObject)
    }.getOrElse {
        it.printStackTrace()
        null
    }
}