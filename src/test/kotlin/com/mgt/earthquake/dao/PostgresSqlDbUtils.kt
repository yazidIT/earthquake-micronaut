package com.mgt.earthquake.dao

import io.r2dbc.spi.ConnectionFactoryOptions
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.Location
import org.flywaydb.core.api.configuration.ClassicConfiguration
import org.testcontainers.containers.MySQLR2DBCDatabaseContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.PostgreSQLR2DBCDatabaseContainer
import org.testcontainers.utility.DockerImageName

object PostgresSqlDbUtils {

    private var mySqlDBContainer: PostgreSQLContainer<*> =

        PostgreSQLContainer(DockerImageName.parse("postgres:12"))
            .withExposedPorts(5432)
            .withUsername("test")
            .withPassword("test")
            .withReuse(true)

    var mySqlR2DBCContainer = PostgreSQLR2DBCDatabaseContainer(mySqlDBContainer)

    fun startMySqlDb() {
        if (!mySqlDBContainer.isRunning) {
            mySqlDBContainer.start()

            val connectionFactoryOptions =
                ConnectionFactoryOptions.parse(mySqlDBContainer.jdbcUrl.replace("jdbc", "r2dbc"))
                .mutate()
                .option(ConnectionFactoryOptions.USER, mySqlDBContainer.username)
                .option(ConnectionFactoryOptions.PASSWORD, mySqlDBContainer.password)
                .build()
            mySqlR2DBCContainer.configure(connectionFactoryOptions)

            mySqlR2DBCContainer.start()
        }

        val fwConfiguration = ClassicConfiguration()
        fwConfiguration.setDataSource(mySqlDbUri, mySqlDbUsername, mySqlDbPassword)
        fwConfiguration.setLocations(
            Location("classpath:/db/migration")
        )

        val flyway = Flyway(fwConfiguration)
        flyway.migrate()
    }

    val mySqlDbUri: String
        get() {
            if (!mySqlDBContainer.isRunning) {
                startMySqlDb()
            }
            return mySqlDBContainer.jdbcUrl
        }
    val mySqlDbUsername: String
        get() {
            if (!mySqlDBContainer.isRunning) {
                startMySqlDb()
            }
            return mySqlDBContainer.username
        }
    val mySqlDbPassword: String
        get() {
            if (!mySqlDBContainer.isRunning) {
                startMySqlDb()
            }
            return mySqlDBContainer.password
        }

    fun closeMySqlDb() {
        mySqlR2DBCContainer.close()
        mySqlDBContainer.close()
    }
}