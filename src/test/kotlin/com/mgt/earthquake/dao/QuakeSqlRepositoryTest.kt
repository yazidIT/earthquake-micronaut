package com.mgt.earthquake.dao

import com.mgt.earthquake.jooqmodel.tables.pojos.Quake
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.slf4j.LoggerFactory

@MicronautTest(startApplication = false, environments = ["integrationtest"])
class QuakeSqlRepositoryTest(

    val underTest: QuakeSqlRepository

) : FunSpec({

    beforeSpec {

    }

    afterSpec{
        underTest.deleteAll()
    }

    val logger = LoggerFactory.getLogger(this::class.java)

    test("create and delete quake should work") {

        val quakepojo1 = Quake(latitude = 3.3, longitude = 101.1, magnitude = 5.7, quakeid = "hga123434",
            quaketime = "2022-07-25T14:44:47.267000", title = "Quake Item in Test Western Sahare")

        val result = underTest.create(quakepojo1)

        Assertions.assertEquals(quakepojo1.quakeid, result.quakeid)
        Assertions.assertNotNull(result.id)
        logger.info(result.toString())

        underTest.delete(result.id!!)
        val result2 = underTest.findById(result.id!!)
        Assertions.assertNull(result2)
    }

    test("find by quakeid should work") {

        val quakepojo1 = Quake(latitude = 3.3, longitude = 101.1, magnitude = 5.7, quakeid = "hga123434",
            quaketime = "2022-07-25T14:44:47.267000", title = "Quake Item in Test Western Sahara")
        val quakepojo2 = Quake(latitude = 3.3, longitude = 101.1, magnitude = 6.7, quakeid = "hga12222",
            quaketime = "2022-07-22T14:44:47.267000", title = "Quake Item in Test Western Malaysia")

        underTest.createQuakes(listOf(quakepojo1, quakepojo2))
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
            quaketime = "2022-07-25T14:44:47.267000", title = "Quake Item in Test Western Sahare")
        val quakepojo2 = Quake(latitude = 3.3, longitude = 101.1, magnitude = 6.7, quakeid = "aaaa1212",
            quaketime = "2022-07-22T14:44:47.267000", title = "Quake Item in Test Western Malaysia")

        underTest.createQuakes(listOf(quakepojo1, quakepojo2))
        val result = underTest.findAll()

        Assertions.assertEquals(2, result.size)
    }
})
