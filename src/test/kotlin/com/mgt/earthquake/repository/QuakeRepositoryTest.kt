package com.mgt.earthquake.repository

import com.mgt.earthquake.dao.QuakeRepository
import com.mgt.earthquake.model.QuakeModel
import com.mgt.earthquake.repository.MongoDbUtils.mongoDbUri
import io.kotest.core.spec.style.FunSpec
import io.micronaut.context.ApplicationContext
import io.micronaut.test.support.TestPropertyProvider
import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId
import org.junit.jupiter.api.Assertions
import org.slf4j.LoggerFactory
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
class QuakeRepositoryTest(

): FunSpec({

    val logger = LoggerFactory.getLogger(QuakeRepositoryTest::class.java)

    val map = mutableMapOf<String, Any>("mongodb.uri" to mongoDbUri)
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

    test("create Quake object and findById - success") {

        // given
        val quake1 = QuakeModel(title = "Quake NE Japan", magnitude = 6.5, latitude = 3.1414,
            longitude = 103.4534, quaketime = "2022-04-22T06:15:23.756000", quakeid = "us6000hfxm")

        underTest.save(quake1)
        val result = underTest.findAll().toList()

        // then
        Assertions.assertTrue(result.isNotEmpty())
        logger.info("$result")

        val result2 = underTest.findById(result[0].id!!)

        // then
        Assertions.assertNotNull(result2)
        Assertions.assertEquals(quake1.title, result2!!.title)
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

}), TestPropertyProvider {

    override fun getProperties(): MutableMap<String, String> {
        MongoDbUtils.startMongoDb()
        return mutableMapOf("mongodb.uri" to mongoDbUri)
    }
}