package com.mgt.earthquake.repository

import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.utility.DockerImageName

object MongoDbUtils {

    var mongoDBContainer: MongoDBContainer =
        MongoDBContainer(DockerImageName.parse("mongo:4.0.10"))
            .withExposedPorts(27017)

    fun startMongoDb() {
        if (!mongoDBContainer.isRunning) {
            mongoDBContainer.start()
        }
    }

    val mongoDbUri: String
        get() {
            if (!mongoDBContainer.isRunning) {
                startMongoDb()
            }
            return mongoDBContainer.replicaSetUrl
        }

    fun closeMongoDb() = mongoDBContainer.close()
}