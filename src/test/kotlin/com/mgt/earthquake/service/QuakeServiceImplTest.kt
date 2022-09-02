package com.mgt.earthquake.service

import com.mgt.earthquake.client.EarthQuakeClient
import com.mgt.earthquake.dao.QuakeRepository
import io.kotest.core.spec.style.FunSpec
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.kotest5.annotation.MicronautTest
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

@MicronautTest
class QuakeServiceImplTest(

    val underTest: QuakeServiceImpl

) : FunSpec({

    val logger = LoggerFactory.getLogger(this::class.java)

    test("yesterday zero hundred and twentythree hundred hour should return correct value") {

        // given

        val result = underTest.yesterdayDateZeroHundredHoursIso()
        val result2 = underTest.yesterdayDateTwentyThreeHundredHoursIso()

        logger.info(result)
        logger.info(result2)
    }

    test("target date 1 march 2022 should return correct value of 28 Feb") {

        // given
        val targetdate = LocalDateTime.of(2022, 3, 1, 1, 9)

        val result = underTest.getZeroHundredHourIso(targetdate)
        val result2 = underTest.getTwentyThreeHundredHourIso(targetdate)

        // then
        logger.info(result)
        logger.info(result2)
        Assertions.assertTrue(result.contains("2022-02-28"))
        Assertions.assertTrue(result2.contains("2022-02-28"))

    }

    test("target date 1 Jan 2022 should return correct value of 31 Dec 2021") {

        // given
        val targetdate = LocalDateTime.of(2022, 1, 1, 1, 9)

        val result = underTest.getZeroHundredHourIso(targetdate)
        val result2 = underTest.getTwentyThreeHundredHourIso(targetdate)

        // then
        logger.info(result)
        logger.info(result2)
        Assertions.assertTrue(result.contains("2021-12-31"))
        Assertions.assertTrue(result2.contains("2021-12-31"))
    }

    test("target date 1 Mar 2020 should return correct value of 29 Feb 2020") {

        // given
        val targetdate = LocalDateTime.of(2020, 3, 1, 1, 9)

        val result = underTest.getZeroHundredHourIso(targetdate)
        val result2 = underTest.getTwentyThreeHundredHourIso(targetdate)

        // then
        logger.info(result)
        logger.info(result2)
        Assertions.assertTrue(result.contains("2020-02-29"))
        Assertions.assertTrue(result2.contains("2020-02-29"))
    }

}) {
    @MockBean(QuakeRepository::class)
    fun quakeRepo(): QuakeRepository = mockk()

    @MockBean(EarthQuakeClient::class)
    fun quakeClient(): EarthQuakeClient = mockk()
}
