package com.mgt.earthquake.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.serde.annotation.Serdeable
import org.bson.types.ObjectId


@Serdeable
@MappedEntity(value = "quake")
data class QuakeModel @JsonCreator constructor(

    @field:Id
    @GeneratedValue
    var id: ObjectId? = null,

    @param:JsonProperty("title")
    var title: String,

    @param:JsonProperty("magnitude")
    var magnitude: Double,

    @param:JsonProperty("quaketime")
    var quaketime: String,

    @param:JsonProperty("latitude")
    var latitude: Double,

    @param:JsonProperty("longitude")
    var longitude: Double,

    @param:JsonProperty("quakeid")
    var quakeid: String
)
