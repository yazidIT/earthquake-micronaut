package com.mgt.earthquake.dao

import com.mgt.earthquake.model.QuakeModel
import io.micronaut.data.mongodb.annotation.MongoRepository
import io.micronaut.data.repository.kotlin.CoroutineCrudRepository
import org.bson.types.ObjectId

@MongoRepository
interface QuakeRepository : CoroutineCrudRepository<QuakeModel, ObjectId>