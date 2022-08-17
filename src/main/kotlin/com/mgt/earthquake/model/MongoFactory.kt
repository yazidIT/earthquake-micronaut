package com.mgt.earthquake.model

import com.mongodb.reactivestreams.client.MongoClients
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton
import org.bson.codecs.configuration.CodecRegistry

@Factory
class MongoFactory {

    @Singleton
    fun mongoCodecRegistry(): CodecRegistry {

        return MongoClients.getDefaultCodecRegistry()
    }
}