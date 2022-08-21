package com.mgt.earthquake.dao

import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.core.naming.Named

@ConfigurationProperties("mongodb")
interface MongoDbConfiguration : Named {

    val collection: String
    val uri: String
}