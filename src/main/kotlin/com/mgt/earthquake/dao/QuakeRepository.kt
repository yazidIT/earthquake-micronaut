package com.mgt.earthquake.dao

import com.mgt.earthquake.model.QuakeModel
import com.mongodb.client.model.Filters.eq
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoCollection
import io.micronaut.data.mongodb.annotation.MongoRepository
import io.micronaut.data.repository.kotlin.CoroutineCrudRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.reactive.asFlow
import org.bson.types.ObjectId

@MongoRepository
abstract class QuakeRepository(
    private val mongoClient: MongoClient
) : CoroutineCrudRepository<QuakeModel, ObjectId> {

    private val collection: MongoCollection<QuakeModel>
        get() = mongoClient.getDatabase("quakedb")
            .getCollection("quake", QuakeModel::class.java)
    abstract fun findByTitle(title: String): Flow<QuakeModel>
    abstract suspend fun findByQuakeid(quakeid: String): QuakeModel?

    override suspend fun findById(id: ObjectId): QuakeModel? {
        return collection
            .find(
                eq("_id", id)
            ).asFlow().firstOrNull()
    }

}