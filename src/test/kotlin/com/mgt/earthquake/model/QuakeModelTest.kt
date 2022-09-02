package com.mgt.earthquake.model

import io.kotest.core.spec.style.FunSpec
import io.micronaut.test.extensions.kotest5.annotation.MicronautTest
import io.micronaut.serde.ObjectMapper
import org.bson.codecs.pojo.ClassModel
import org.junit.jupiter.api.Assertions

@MicronautTest
class QuakeModelTest (

    val objectMapper: ObjectMapper,
): FunSpec({

    test("serialisation and deserialisation QuakeModel should work") {

        // given
        val quake1 = QuakeModel(title = "Quake NE Japan", magnitude = 6.5, latitude = 3.1414,
            longitude = 103.4534, quaketime = "2022-04-22T06:15:23.756000", quakeid = "us6000hfxm")

        val serResult = objectMapper.writeValueAsString(quake1)
        val desResult = objectMapper.readValue(serResult, QuakeModel::class.java)
        println(serResult)

        // then
        Assertions.assertEquals(quake1.title, desResult!!.title)

    }

    test("check classmodel") {
        val classmodel = ClassModel.builder(QuakeModel::class.java).build()
        println(classmodel.type)
        println(classmodel.propertyModels)
    }

})
