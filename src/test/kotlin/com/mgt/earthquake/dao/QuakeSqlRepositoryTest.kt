package com.mgt.earthquake.dao

import com.mgt.earthquake.jooqmodel.tables.pojos.Quake
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.micronaut.context.ApplicationContext
import io.micronaut.test.support.TestPropertyProvider
import kotlinx.coroutines.flow.toList
import org.junit.jupiter.api.Assertions
import org.slf4j.LoggerFactory


class QuakeSqlRepositoryTest: FunSpec({

    val map = mutableMapOf<String, Any>(
        "r2dbc.datasources.rx-quake.url" to PostgresSqlDbUtils.mySqlDbUri.replace("jdbc", "r2dbc"),
        "r2dbc.datasources.rx-quake.username" to PostgresSqlDbUtils.mySqlDbUsername,
        "r2dbc.datasources.rx-quake.password" to PostgresSqlDbUtils.mySqlDbPassword
    )
    val context = ApplicationContext.run(map)

    val underTest = context.getBean(QuakeSqlRepository::class.java)

    beforeSpec {
    }

    afterSpec{
    }

    afterEach {
        underTest.deleteAll()
    }

    val logger = LoggerFactory.getLogger(this::class.java)

    test("should successfully start") {
        Assertions.assertTrue(context.isRunning)
    }

    test("list all should work") {

        val result = underTest.findAll().toList()

        result shouldBe emptyList()
    }

    test("create and delete quake should work") {

        val quakepojo1 = Quake(latitude = 3.3, longitude = 101.1, magnitude = 5.7, quakeid = "hga123434",
            quaketime = "2022-07-25T14:44:47.267000", title = "Quake Item in Test Western Sahare")

        val result = underTest.create(quakepojo1)!!

        Assertions.assertEquals(quakepojo1.quakeid, result.quakeid)
        Assertions.assertNotNull(result.id)
        logger.info("$result")

        underTest.delete(result.id!!)
        val result2 = underTest.findById(result.id!!)
        Assertions.assertNull(result2)
    }

    test("create multiple quakes and find by quakeid should work") {

        val quakepojo1 = Quake(latitude = 3.3, longitude = 101.1, magnitude = 5.7, quakeid = "hga123434",
            quaketime = "2022-07-25T14:44:47.267000", title = "Quake Item in Test Western Sahara")
        val quakepojo2 = Quake(latitude = 3.3, longitude = 101.1, magnitude = 6.7, quakeid = "hga12222",
            quaketime = "2022-07-22T14:44:47.267000", title = "Quake Item in Test Western Malaysia")

        val createresult = underTest.createQuakes(listOf(quakepojo1, quakepojo2))
        createresult.map { logger.info("$it") }

        val result = underTest.findByQuakeId("hga12222")

        Assertions.assertNotNull(result)
        Assertions.assertEquals("Quake Item in Test Western Malaysia", result!!.title)
    }

    test("update quake should work") {

        val quakepojo1 = Quake(latitude = 3.3, longitude = 101.1, magnitude = 5.7, quakeid = "hga123434",
            quaketime = "2022-07-25T14:44:47.267000", title = "Quake Item in Test Western Sahara")

        underTest.create(quakepojo1)
        val quakeupdate = underTest.findByQuakeId("hga123434")
        quakeupdate!!.title = "Quake Item in Test Western India"
        underTest.update(quakeupdate)

        val result = underTest.findByQuakeId("hga123434")

        Assertions.assertNotNull(result)
        Assertions.assertEquals("Quake Item in Test Western India", result!!.title)
    }

    test("create multiple quakes and list all should work") {

        val quakepojo1 = Quake(latitude = 3.3, longitude = 101.1, magnitude = 5.7, quakeid = "hga123434",
            quaketime = "2022-07-25T14:44:47.267000", title = "Quake Item in Test Western Sahara")
        val quakepojo2 = Quake(latitude = 3.3, longitude = 101.1, magnitude = 6.7, quakeid = "aaaa1212",
            quaketime = "2022-07-22T14:44:47.267000", title = "Quake Item in Test Western Malaysia")

        underTest.createQuakes(listOf(quakepojo1, quakepojo2))
        val result = underTest.findAll().toList()

        result.size shouldBe 2
    }

    test("create multiple quakes and delete all but one should work") {

        val quakepojo1 = Quake(latitude = 3.3, longitude = 101.1, magnitude = 5.7, quakeid = "hga123434",
            quaketime = "2022-07-25T14:44:47.267000", title = "Quake Item in Test Western Sahara")
        val quakepojo2 = Quake(latitude = 3.3, longitude = 101.1, magnitude = 6.7, quakeid = "aaaa1212",
            quaketime = "2022-07-22T14:44:47.267000", title = "Quake Item in Test Western Malaysia")
        val quakepojo3 = Quake(latitude = 5.3, longitude = 108.1, magnitude = 7.7, quakeid = "bbaa1212",
            quaketime = "2022-07-22T14:44:47.267000", title = "Quake Item in Test Western Samoa")
        val quakepojo4 = Quake(latitude = 11.3, longitude = 87.1, magnitude = 7.2, quakeid = "bbcc1212",
            quaketime = "2022-07-22T14:44:47.267000", title = "Quake Item in Test Western Iran")

        val quakelistrecord = underTest.createQuakes(listOf(quakepojo1, quakepojo2, quakepojo3, quakepojo4))
        Assertions.assertEquals(4, quakelistrecord.size)

        val listtodelete = listOf(quakelistrecord[0]!!.id!!, quakelistrecord[1]!!.id!!, quakelistrecord[2]!!.id!!)
        underTest.delete(listtodelete)
        val lastlist = underTest.findAll().toList()

        lastlist.size shouldBe 1
    }

}), TestPropertyProvider {

    override fun getProperties(): MutableMap<String, String> {
        PostgresSqlDbUtils.startMySqlDb()
        return mutableMapOf()
    }
}
