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

            var objectId: ObjectId? = null
            var title: String = ""
            var magnitude: Double = 0.0
            var quaketime: String = ""
            var latitude: Double = 0.0
            var longitude: Double = 0.0
            var quakeid: String = ""

            var objectKey = it.decodeKey()
            while( objectKey != null) {

                when (objectKey) {
                    "id" -> {
                        val objectidStr = it.decodeString()
                        objectId = ObjectId(objectidStr)
                    }

                    "title" -> title = it.decodeString()
                    "magnitude" -> magnitude = it.decodeDouble()
                    "quaketime" -> quaketime = it.decodeString()
                    "latitude" -> latitude = it.decodeDouble()
                    "longitude" -> longitude = it.decodeDouble()
                    "quakeid" -> quakeid = it.decodeString()
                }
                objectKey = it.decodeKey()
            }

            return QuakeModel(objectId, title, magnitude, quaketime, latitude, longitude, quakeid)
        }
    }
}