package com.mgt.earthquake.dao

import io.r2dbc.spi.ConnectionFactoryOptions
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.containers.MySQLR2DBCDatabaseContainer
import org.testcontainers.utility.DockerImageName

object MySqlDbUtils {

    private var mySqlDBContainer: MySQLContainer<*> =

        MySQLContainer(DockerImageName.parse("mysql:8"))
            .withExposedPorts(3306)
            .withUsername("test")
            .withPassword("test")
            .withReuse(true)

    var mySqlR2DBCContainer = MySQLR2DBCDatabaseContainer(mySqlDBContainer)

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