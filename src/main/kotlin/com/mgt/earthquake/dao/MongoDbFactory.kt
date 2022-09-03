package com.mgt.earthquake.dao

import com.mgt.earthquake.model.QuakeModelCodec
import com.mongodb.MongoClientSettings
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.configuration.CodecRegistry
import org.bson.codecs.pojo.PojoCodecProvider

@Factory
class MongoDbFactory {

    var uri = "mongodb://localhost:27017"

    @Singleton
    fun mongoClient(): MongoClient = MongoClients.create(mongodbSettings())

    fun mongodbSettings(): MongoClientSettings {

        val pojoCodecRegistry: CodecRegistry = CodecRegistries.fromRegistries(
            CodecRegistries.fromCodecs(QuakeModelCodec()), // <---- this is the custom codec
            MongoClientSettings.getDefaultCodecRegistry(),
            CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
        )

        val connectionString = ConnectionString(uri)
        return MongoClientSettings.builder()
            .codecRegistry(pojoCodecRegistry)
            .applyConnectionString(connectionString)
            .build()
    }

}