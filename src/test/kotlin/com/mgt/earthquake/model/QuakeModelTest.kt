package com.mgt.earthquake.model

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.FunSpec
import io.micronaut.test.extensions.kotest5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions

@MicronautTest(transactional = false)
class QuakeModelTest (

    val objectMapper: ObjectMapper,
): FunSpec({

    xtest("serialisation and deserialisation QuakeModel should work") {

        // given
        val quake1 = QuakeModel(title = "Quake NE Japan", magnitude = 6.5, latitude = 3.1414,
            longitude = 103.4534, quaketime = "2022-04-22T06:15:23.756000", quakeid = "us6000hfxm")

        val serResult = objectMapper.writeValueAsString(quake1)
        val desResult = objectMapper.readValue(serResult, QuakeModel::class.java)
        println(serResult)

        // then
        Assertions.assertEquals(quake1.title, desResult.title)

    }

})
