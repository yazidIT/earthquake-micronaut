package com.mgt.earthquake.service

import com.mgt.earthquake.client.EarthQuakeClient
import com.mgt.earthquake.dao.QuakeRepository
import io.kotest.core.spec.style.FunSpec
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import io.mockk.mockk
import org.slf4j.LoggerFactory

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

}) {
    @MockBean(QuakeRepository::class)
    fun quakeRepo(): QuakeRepository = mockk()

    @MockBean(EarthQuakeClient::class)
    fun quakeClient(): EarthQuakeClient = mockk()
}
