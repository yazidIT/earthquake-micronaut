package com.mgt.earthquake.model

import io.micronaut.core.annotation.Introspected

@Introspected
data class QuakeResponse(
    val features: List<QuakeResponseFeature>
)

@Introspected
data class QuakeResponseFeature (
    val id: String,
    val properties: QuakeProperty,
    val geometry: QuakeGeometry
)

@Introspected
data class QuakeProperty(
    val time: Long,
    val title: String,
    val mag: Double
)

@Introspected
data class QuakeGeometry(
    val coordinates: ArrayList<Double>
)