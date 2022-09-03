package com.mgt.earthquake.model

import io.micronaut.core.type.Argument
import io.micronaut.serde.*
import jakarta.inject.Singleton
import org.bson.types.ObjectId

@Singleton
class QuakeSerdes: Serde<QuakeModel> {

    override fun serialize(
        encoder: Encoder,
        context: Serializer.EncoderContext,
        type: Argument<out QuakeModel>,
        value: QuakeModel
    ) {
        encoder.encodeObject(type).use {
            it.encodeKey("id")
            value.id?.let { objectid ->
                it.encodeString(objectid.toString())
            } ?: it.encodeString(ObjectId().toString())
            it.encodeKey("title")
            it.encodeString(value.title)
            it.encodeKey("magnitude")
            it.encodeDouble(value.magnitude)
            it.encodeKey("quaketime")
            it.encodeString(value.quaketime)
            it.encodeKey("latitude")
            it.encodeDouble(value.latitude)
            it.encodeKey("longitude")
            it.encodeDouble(value.longitude)
            it.encodeKey("quakeid")
            it.encodeString(value.quakeid)
        }
    }

    override fun deserialize(
        decoder: Decoder,
        context: Deserializer.DecoderContext,
        type: Argument<in QuakeModel>
    ): QuakeModel? {

        decoder.decodeObject(type).use {
            it.decodeKey()
            val objectidStr = it.decodeString()
            val objectId = ObjectId(objectidStr)
            it.decodeKey()
            val title = it.decodeString()
            it.decodeKey()
            val magnitude = it.decodeDouble()
            it.decodeKey()
            val quaketime = it.decodeString()
            it.decodeKey()
            val latitude = it.decodeDouble()
            it.decodeKey()
            val longitude = it.decodeDouble()
            it.decodeKey()
            val quakeid = it.decodeString()

            return QuakeModel(objectId, title, magnitude, quaketime, latitude, longitude, quakeid)
        }
    }
}