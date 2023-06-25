package com.mgt.earthquake.model

import io.micronaut.serde.annotation.Serdeable
import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonProperty

@Serdeable
data class QuakeResponse @BsonCreator constructor(
    @param:BsonProperty("features")
    val features: List<QuakeResponseFeature> = emptyList()
)

@Serdeable
data class QuakeResponseFeature @BsonCreator constructor(
    @param:BsonProperty("id")
    val id: String,
    @param:BsonProperty("properties")
    val properties: QuakeProperty,
    @param:BsonProperty("geometry")
    val geometry: QuakeGeometry
)

@Serdeable
data class QuakeProperty @BsonCreator constructor(
    @param:BsonProperty("time")
    val time: Long,
    @param:BsonProperty("title")
    val title: String,
    @param:BsonProperty("mag")
    val mag: Double
)

@Serdeable
data class QuakeGeometry @BsonCreator constructor(
    @param:BsonProperty("coordinates")
    val coordinates: ArrayList<Double> = ArrayList()
)