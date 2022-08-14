package com.mgt.earthquake.dao

import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactoryOptions
import jakarta.inject.Named
import org.jooq.Configuration
import org.jooq.DSLContext
import org.jooq.impl.DSL

@Factory
class JooqFactory(
    private val jooqConfig: Configuration,
    private val r2dbcConfiguration: R2dbcConfiguration
) {

    @Bean
    @Named("CustomJooqConfig")
    fun jooqConfiguration() : Configuration {

//        jooqConfig.set(DefaultRecordListenerProvider(insertListener))
        return jooqConfig
    }

    @Bean
    @Named("R2dbcJooqDslContext")
    fun r2dbcJooqDsl(): DSLContext {
        val connectionFactory = ConnectionFactories.get(
            ConnectionFactoryOptions.parse(r2dbcConfiguration.url!!)
                .mutate()
                .option(ConnectionFactoryOptions.USER, r2dbcConfiguration.username!!)
                .option(ConnectionFactoryOptions.PASSWORD, r2dbcConfiguration.password!!)
                .build()
        )

        return DSL.using(connectionFactory)
    }
}