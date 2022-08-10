package com.mgt.earthquake.model

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class QuakeDTOList (
    val quakeList: List<QuakeDTO> = listOf()
)