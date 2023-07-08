package com.mgt.earthquake.model

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class QuakeModelDTO(
    var id: String,
    var title: String,
    var magnitude: Double,
    var quaketime: String,
    var latitude: Double,
    var longitude: Double,
    var quakeid: String
) {
    constructor(quakeModel: QuakeModel): this(
        quakeModel.id?.let { it.toHexString() } ?: "-",
        quakeModel.title,
        quakeModel.magnitude,
        quakeModel.quaketime,
        quakeModel.latitude,
        quakeModel.longitude,
        quakeModel.quakeid
    )
}
