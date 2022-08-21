package com.mgt.earthquake.model

import com.mongodb.reactivestreams.client.MongoClients
import jakarta.inject.Singleton
import org.bson.BsonReader
import org.bson.BsonWriter
import org.bson.Document
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext

@Singleton
class QuakeModelCodec: Codec<QuakeModel> {

    private val documentCodec = MongoClients.getDefaultCodecRegistry().get(Document::class.java)
    override fun encode(writer: BsonWriter?, value: QuakeModel?, encoderContext: EncoderContext?) {
        val document = Document()
        document["title"] = value!!.title
        document["magnitude"] = value.magnitude
        document["latitude"] = value.latitude
        document["longitude"] = value.longitude
        document["quaketime"] = value.quaketime
        document["quakeid"] = value.quakeid
        documentCodec.encode(writer, document, encoderContext)
    }

    override fun getEncoderClass(): Class<QuakeModel> {
        return QuakeModel::class.java
    }

    override fun decode(reader: BsonReader?, decoderContext: DecoderContext?): QuakeModel {

        val document = documentCodec.decode(reader, decoderContext)

        return QuakeModel(
            id = document.getObjectId("_id"),
            title = document.getString("title"), magnitude = document.getDouble("magnitude"),
            latitude = document.getDouble("latitude"), longitude = document.getDouble("longitude"),
            quaketime = document.getString("quaketime"), quakeid = document.getString("quakeid")
        )
    }
}
