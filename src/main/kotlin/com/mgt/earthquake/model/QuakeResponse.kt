package com.mgt.earthquake.model

import com.fasterxml.jackson.annotation.JsonProperty
import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class QuakeResponse (
    @param:JsonProperty("features")
    val features: List<QuakeResponseFeature>
)

@Serdeable
data class QuakeResponseFeature (
    @param:JsonProperty("id")
    val id: String,
    @param:JsonProperty("properties")
    val properties: QuakeProperty,
    @param:JsonProperty("geometry")
    val geometry: QuakeGeometry
)

@Serdeable
data class QuakeProperty(
    @param:JsonProperty("time")
    val time: Long,
    @param:JsonProperty("title")
    val title: String,
    @param:JsonProperty("mag")
    val mag: Double
)

@Serdeable
data class QuakeGeometry(
    @param:JsonProperty("coordinates")
    val coordinates: ArrayList<Double>
)