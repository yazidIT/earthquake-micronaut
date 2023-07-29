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
)

fun QuakeModel.toDTO(): QuakeModelDTO = QuakeModelDTO(
    this.id?.let { it.toHexString() } ?: "-",
    this.title,
    this.magnitude,
    this.quaketime,
    this.latitude,
    this.longitude,
    this.quakeid
)