package com.mgt.earthquake.model

import com.mgt.earthquake.dao.MongoDbConfiguration
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import io.micronaut.context.annotation.Factory
import jakarta.inject.Named
import jakarta.inject.Singleton
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.configuration.CodecRegistry

@Factory
class MongoFactory(
    private val mongoDbConfig: MongoDbConfiguration
) {

    @Singleton
    fun mongoCodecRegistry(): CodecRegistry {

        return MongoClients.getDefaultCodecRegistry()
    }

    @Named("earthquake")
    @Singleton
    fun mongoClient(): MongoClient = MongoClients.create(mongodbSettings())

    fun mongodbSettings(): MongoClientSettings {

        val pojoCodecRegistry: CodecRegistry = CodecRegistries.fromRegistries(
            MongoClientSettings.getDefaultCodecRegistry()
        )

        val connectionString = ConnectionString(mongoDbConfig.uri)
        return MongoClientSettings.builder()
            .codecRegistry(pojoCodecRegistry)
            .applyConnectionString(connectionString)
            .build()
    }
}