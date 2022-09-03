package com.mgt.earthquake.model

import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.serde.annotation.Serdeable
import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty
import org.bson.types.ObjectId


@Serdeable
@MappedEntity(value = "quake")
data class QuakeModel @BsonCreator constructor(

    @GeneratedValue
    @field:Id
    @param:BsonId
    var id: ObjectId? = null,

    @param:BsonProperty("title")
    var title: String,

    @param:BsonProperty("magnitude")
    var magnitude: Double,

    @param:BsonProperty("quaketime")
    var quaketime: String,

    @param:BsonProperty("latitude")
    var latitude: Double,

    @param:BsonProperty("longitude")
    var longitude: Double,

    @param:BsonProperty("quakeid")
    var quakeid: String
)
