package com.mgt.earthquake.model

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class QuakeDTO(
    var title: String,
    var magnitude: Double,
    var quaketime: String,
    var latitude: Double,
    var longitude: Double,
    var quakeid: String
)
