package com.mgt.earthquake.model

data class QuakeDTO(
    var title: String,
    var magnitude: Double,
    var quaketime: String,
    var latitude: Double,
    var longitude: Double,
    var quakeid: String
)
