package com.mgt.earthquake.dao

import com.mgt.earthquake.dao.MongoDbUtils.mongoDbUri
import com.mgt.earthquake.model.QuakeModel
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.micronaut.context.ApplicationContext
import io.micronaut.test.support.TestPropertyProvider
import kotlinx.coroutines.flow.toList
import org.junit.jupiter.api.Assertions
import org.slf4j.LoggerFactory

class QuakeRepositoryTest: FunSpec({

    val logger = LoggerFactory.getLogger(QuakeRepositoryTest::class.java)

    val map = mutableMapOf<String, Any>(
        "mongodb.uri" to mongoDbUri,
        "mongodb.name" to "test",
    )
    val context = ApplicationContext.run(map)

    val underTest = context.getBean(QuakeRepository::class.java)

    beforeSpec {
    }

    afterSpec {
        MongoDbUtils.closeMongoDb()
    }

    afterEach {
        underTest.deleteAll()
    }

    test("should return empty") {

        val result = underTest.findAll().toList()

        Assertions.assertEquals(result.size, 0)
    }

    test("create Quake object and findById should work") {

        // given
        val quake1 = QuakeModel(title = "Quake NE Japan", magnitude = 6.5, latitude = 3.1414,
            longitude = 103.4534, quaketime = "2022-04-22T06:15:23.756000", quakeid = "us6000hfxm")

        underTest.save(quake1)
        val result = underTest.findAll().toList()
        logger.info("$result")

        // then
        Assertions.assertTrue(result.isNotEmpty())

        val result2 = underTest.findById(result[0].id!!)

        // then
        Assertions.assertNotNull(result2)
        Assertions.assertEquals(quake1.title, result2!!.title)
    }

    test("create Quake object with existing quakeid should fail") {

        // given
        val quake1 = QuakeModel(title = "Quake NE Japan", magnitude = 6.5, latitude = 3.1414,
            longitude = 103.4534, quaketime = "2022-04-22T06:15:23.756000", quakeid = "us6000hfxm")

        underTest.save(quake1)

        // given
        val quake2 = QuakeModel(title = "Quake NE Japan 2", magnitude = 6.7, latitude = 25.1234,
            longitude = 103.4534, quaketime = "2022-04-22T06:15:23.756000", quakeid = "us6000hfxm")

        val createresult = underTest.save(quake2)
        println(createresult)

        createresult shouldBe null
    }

    test("create Quake object, findByTitle & findByQuakeid - success") {

        // given
        val quake1 = QuakeModel(title = "Quake NE Japan", magnitude = 6.5, latitude = 3.1414,
            longitude = 103.4534, quaketime = "2022-04-22T06:15:23.756000", quakeid = "us6000hfxm")

        underTest.save(quake1)
        val result = underTest.findByTitle(quake1.title).toList()

        // then
        Assertions.assertTrue(result.isNotEmpty())
        logger.info("$result")

        val result2 = underTest.findByQuakeid(quake1.quakeid)
        Assertions.assertNotNull(result2)
    }

    test("create Quake object with similar titles, findByTitle should return correct number") {

        // given
        val quake1 = QuakeModel(title = "Quake NE Japan", magnitude = 6.5, latitude = 3.1414,
            longitude = 103.4534, quaketime = "2022-04-22T06:15:23.756000", quakeid = "us6000hfxm")
        val quake2 = QuakeModel(title = "Quake NE Japan", magnitude = 7.5, latitude = 8.1414,
            longitude = 87.4534, quaketime = "2022-04-22T06:15:23.756000", quakeid = "us6aa0hfxm")

        underTest.saveAll(listOf(quake1, quake2)).toList()
        val result = underTest.findByTitle(quake1.title).toList()

        // then
        Assertions.assertEquals(2, result.size)

        val result2 = underTest.customFindByTitle(quake1.title).toList()

        Assertions.assertEquals(2, result2.size)
    }

    test("create Quake objects, customfindById should work") {

        // given
        val quake1 = QuakeModel(title = "Quake NE Japan1", magnitude = 6.0, latitude = -4.1414,
            longitude = 103.4534, quaketime = "2022-04-22T06:15:23.756000", quakeid = "us6000hfxm")
        val quake2 = QuakeModel(title = "Quake NE Japan2", magnitude = 7.5, latitude = 8.1414,
            longitude = 87.4534, quaketime = "2022-04-22T06:15:23.756000", quakeid = "us6aa0hllm")

        underTest.saveAll(listOf(quake1, quake2)).toList()
        val result = underTest.findAll().toList()

        // then
        Assertions.assertEquals(2, result.size)

        val result2 = underTest.customFindById(result[0].id!!)

        Assertions.assertNotNull(result2)
    }

    test("create Quake objects, and delete By Id - success") {

        // given
        val quake1 = QuakeModel(title = "Quake NE Japan", magnitude = 6.5, latitude = 3.1414,
            longitude = 103.4534, quaketime = "2022-04-22T06:15:23.756000", quakeid = "us6000hfxm")
        val quake2 = QuakeModel(title = "Quake NE Japan2", magnitude = 6.9, latitude = 5.73455,
            longitude = 90.232323, quaketime = "2022-05-22T06:15:23.756000", quakeid = "us6000hfjk")

        underTest.saveAll(listOf(quake1, quake2)).toList()
        val result = underTest.findAll().toList()

        // then
        Assertions.assertEquals(2, result.size)
        logger.info("$result")

        underTest.deleteById(result[0].id!!)
        val result2 = underTest.findAll().toList()

        Assertions.assertEquals(1, result2.size)
        Assertions.assertEquals(result[1].id, result2[0].id)
    }

    test("create Quake objects, and get list of latest should return correct object") {

        // given
        val quake1 = QuakeModel(title = "Quake NE Japan1", magnitude = 6.5, latitude = 3.1414,
            longitude = 103.4534, quaketime = "2022-04-22T06:15:23.756000", quakeid = "us6000hfxm")
        val quake2 = QuakeModel(title = "Quake NE Japan2", magnitude = 6.9, latitude = 5.73455,
            longitude = 90.232323, quaketime = "2022-05-22T06:15:23.756000", quakeid = "us6000hfjk")
        val quake3 = QuakeModel(title = "Quake NE Japan3", magnitude = 6.5, latitude = 3.1414,
            longitude = 103.4534, quaketime = "2022-04-22T06:15:23.756000", quakeid = "us6000hfxn")
        val quake4 = QuakeModel(title = "Quake NE Japan4", magnitude = 6.9, latitude = 5.73455,
            longitude = 90.232323, quaketime = "2022-05-22T06:15:23.756000", quakeid = "us6000hfjl")

        underTest.saveAll(listOf(quake1, quake2, quake3, quake4)).toList()
        val result = underTest.findLatestNumber(2).toList()

        // then
        result.size shouldBe 2
        logger.info("$result")
        result[0].quakeid shouldBe quake4.quakeid
        result[1].quakeid shouldBe quake3.quakeid

        val finalcount = result.filter { it.quakeid == "us6000hfxn" || it.quakeid == "us6000hfjl" }
            .map { logger.info("$it") }
            .count()

        finalcount shouldBe 2
    }

}), TestPropertyProvider {

    override fun getProperties(): MutableMap<String, String> {
        MongoDbUtils.startMongoDb()
        return mutableMapOf()
    }
}