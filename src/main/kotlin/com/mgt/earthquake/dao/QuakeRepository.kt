package com.mgt.earthquake.dao

import com.mgt.earthquake.model.QuakeModel
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Sorts
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoCollection
import io.micronaut.data.mongodb.annotation.MongoRepository
import io.micronaut.data.repository.kotlin.CoroutineCrudRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.bson.types.ObjectId


@MongoRepository
abstract class QuakeRepository(
    private val mongoClient: MongoClient,
    private val mongoConf: MongoDbConfiguration
) : CoroutineCrudRepository<QuakeModel, ObjectId> {

    private val collection: MongoCollection<QuakeModel>
        get() = mongoClient.getDatabase(mongoConf.name)
            .getCollection(mongoConf.collection, QuakeModel::class.java)
    abstract fun findByTitle(title: String): Flow<QuakeModel>
    abstract suspend fun findByQuakeid(quakeid: String): QuakeModel?
    abstract override suspend fun findById(id: ObjectId): QuakeModel?

    fun customFindByTitle(title: String): Flow<QuakeModel> =

        collection.find(eq("title", title))
            .asFlow()

    suspend fun customFindById(id: ObjectId): QuakeModel? =

        collection.find(eq("_id", id)).awaitFirstOrNull()

    fun findLatestNumber(number: Int): Flow<QuakeModel> =

        collection.find().sort(Sorts.descending("_id")).limit(number).asFlow()

    suspend fun save(quakeModel: QuakeModel): QuakeModel? =

        findByQuakeid(quakeModel.quakeid).also {quakeItem ->

            return when(quakeItem) {
                null ->
                    collection.insertOne(quakeModel).awaitFirstOrNull()?.let {
                        quakeModel.copy(id = it.insertedId!!.asObjectId().value)
                    }
                else -> null
            }
        }
}