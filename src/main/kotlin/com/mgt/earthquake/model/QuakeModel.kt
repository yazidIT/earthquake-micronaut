package com.mgt.earthquake.model

import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import org.bson.types.ObjectId

@MappedEntity
data class QuakeModel(

    @field:Id
    @GeneratedValue
    var id: ObjectId,

    var title: String,
    var magnitude: Double,
    var quaketime: String,
    var latitude: Double,
    var longitude: Double,
    var quakeid: String
)
