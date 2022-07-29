package com.mgt.earthquake.model

import com.fasterxml.jackson.annotation.JsonProperty
import io.micronaut.core.annotation.Introspected

@Introspected
data class QuakeResponse (
    @param:JsonProperty("features")
    val features: List<QuakeResponseFeature>
)

@Introspected
data class QuakeResponseFeature (
    @param:JsonProperty("id")
    val id: String,
    @param:JsonProperty("properties")
    val properties: QuakeProperty,
    @param:JsonProperty("geometry")
    val geometry: QuakeGeometry
)

@Introspected
data class QuakeProperty(
    @param:JsonProperty("time")
    val time: Long,
    @param:JsonProperty("title")
    val title: String,
    @param:JsonProperty("mag")
    val mag: Double
)

@Introspected
data class QuakeGeometry(
    @param:JsonProperty("coordinates")
    val coordinates: ArrayList<Double>
)