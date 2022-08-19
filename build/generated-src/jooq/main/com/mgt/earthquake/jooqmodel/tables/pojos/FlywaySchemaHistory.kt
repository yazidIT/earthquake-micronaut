/*
 * This file is generated by jOOQ.
 */
package com.mgt.earthquake.jooqmodel.tables.pojos


import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

import java.io.Serializable
import java.time.LocalDateTime


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
@Entity
@Table(
    name = "flyway_schema_history",
    indexes = [
        Index(name = "flyway_schema_history_s_idx", columnList = "success ASC")
    ]
)
data class FlywaySchemaHistory(
    @get:Id
    @get:Column(name = "installed_rank", nullable = false, precision = 10)
    @get:NotNull
    var installedRank: Int? = null,
    @get:Column(name = "version", length = 50)
    @get:Size(max = 50)
    var version: String? = null,
    @get:Column(name = "description", nullable = false, length = 200)
    @get:NotNull
    @get:Size(max = 200)
    var description: String? = null,
    @get:Column(name = "type", nullable = false, length = 20)
    @get:NotNull
    @get:Size(max = 20)
    var type: String? = null,
    @get:Column(name = "script", nullable = false, length = 1000)
    @get:NotNull
    @get:Size(max = 1000)
    var script: String? = null,
    @get:Column(name = "checksum", precision = 10)
    var checksum: Int? = null,
    @get:Column(name = "installed_by", nullable = false, length = 100)
    @get:NotNull
    @get:Size(max = 100)
    var installedBy: String? = null,
    @get:Column(name = "installed_on", nullable = false)
    @get:NotNull
    var installedOn: LocalDateTime? = null,
    @get:Column(name = "execution_time", nullable = false, precision = 10)
    @get:NotNull
    var executionTime: Int? = null,
    @get:Column(name = "success", nullable = false, precision = 3)
    @get:NotNull
    var success: Byte? = null
): Serializable {


    override fun toString(): String {
        val sb = StringBuilder("FlywaySchemaHistory (")

        sb.append(installedRank)
        sb.append(", ").append(version)
        sb.append(", ").append(description)
        sb.append(", ").append(type)
        sb.append(", ").append(script)
        sb.append(", ").append(checksum)
        sb.append(", ").append(installedBy)
        sb.append(", ").append(installedOn)
        sb.append(", ").append(executionTime)
        sb.append(", ").append(success)

        sb.append(")")
        return sb.toString()
    }
}
