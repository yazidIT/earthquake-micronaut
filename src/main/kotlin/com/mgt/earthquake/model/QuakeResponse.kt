package com.mgt.earthquake.model

import io.micronaut.serde.annotation.Serdeable
import org.bson.codecs.pojo.annotations.BsonProperty

@Serdeable
data class QuakeResponse (
    @param:BsonProperty("features")
    val features: List<QuakeResponseFeature>
)

@Serdeable
data class QuakeResponseFeature (
    @param:BsonProperty("id")
    val id: String,
    @param:BsonProperty("properties")
    val properties: QuakeProperty,
    @param:BsonProperty("geometry")
    val geometry: QuakeGeometry
)

@Serdeable
data class QuakeProperty(
    @param:BsonProperty("time")
    val time: Long,
    @param:BsonProperty("title")
    val title: String,
    @param:BsonProperty("mag")
    val mag: Double
)

@Serdeable
data class QuakeGeometry(
    @param:BsonProperty("coordinates")
    val coordinates: ArrayList<Double>
)