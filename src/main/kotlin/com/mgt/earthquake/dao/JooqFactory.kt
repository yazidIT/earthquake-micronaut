package com.mgt.earthquake.dao

import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import jakarta.inject.Named
import org.jooq.Configuration

@Factory
class JooqFactory(
    private val jooqConfig: Configuration
) {

    @Bean
    @Named("CustomJooqConfig")
    fun jooqConfiguration() : Configuration {

//        jooqConfig.set(DefaultRecordListenerProvider(insertListener))
        return jooqConfig
    }
}