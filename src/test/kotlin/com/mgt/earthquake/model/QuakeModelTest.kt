package com.mgt.earthquake.model

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.micronaut.serde.ObjectMapper
import io.micronaut.test.extensions.kotest5.annotation.MicronautTest
import org.bson.codecs.pojo.ClassModel
import org.bson.types.ObjectId

@MicronautTest(transactional = false)
class QuakeModelTest(
    val objectMapper: ObjectMapper
) : FunSpec({

    test("serialisation and deserialisation QuakeModel should work") {

        // given
        val quake1 = QuakeModel(id = ObjectId(), title = "Quake NE Japan", magnitude = 6.5, latitude = 3.1414,
            longitude = 103.4534, quaketime = "2022-04-22T06:15:23.756000", quakeid = "us6000hfxm")

        val serResult = objectMapper.writeValueAsString(quake1)
        val desResult = objectMapper.readValue(serResult, QuakeModel::class.java)

        // then
        desResult.title shouldBe quake1.title

        // given
        val quake2 = QuakeModel(title = "Quake NE Japan", magnitude = 6.5, latitude = 3.1414,
            longitude = 103.4534, quaketime = "2022-04-22T06:15:23.756000", quakeid = "us6000hfxm")

        val serResult2 = objectMapper.writeValueAsString(quake2)
        val desResult2 = objectMapper.readValue(serResult2, QuakeModel::class.java)

        // then
        desResult2.title shouldBe quake2.title
    }

    test("check classmodel") {
        val classmodel = ClassModel.builder(QuakeModel::class.java).build()
        println(classmodel.type)
        println(classmodel.propertyModels)
    }

})
