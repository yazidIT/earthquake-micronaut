package com.mgt.earthquake.dao

import com.mgt.earthquake.model.QuakeModel
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoCollection
import io.micronaut.data.mongodb.annotation.MongoRepository
import io.micronaut.data.repository.kotlin.CoroutineCrudRepository
import kotlinx.coroutines.flow.Flow
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
    abstract override suspend fun findById(id: ObjectId): QuakeModel?
}