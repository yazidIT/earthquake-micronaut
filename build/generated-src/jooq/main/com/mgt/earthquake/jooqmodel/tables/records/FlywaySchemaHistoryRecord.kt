/*
 * This file is generated by jOOQ.
 */
package com.mgt.earthquake.jooqmodel.tables.records


import com.mgt.earthquake.jooqmodel.tables.FlywaySchemaHistory

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

import java.time.LocalDateTime

import org.jooq.Field
import org.jooq.Record1
import org.jooq.Record10
import org.jooq.Row10
import org.jooq.impl.UpdatableRecordImpl


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
open class FlywaySchemaHistoryRecord() : UpdatableRecordImpl<FlywaySchemaHistoryRecord>(FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY), Record10<Int?, String?, String?, String?, String?, Int?, String?, LocalDateTime?, Int?, Byte?> {

    @get:Id
    @get:Column(name = "installed_rank", nullable = false, precision = 10)
    @get:NotNull
    var installedRank: Int?
        set(value): Unit = set(0, value)
        get(): Int? = get(0) as Int?

    @get:Column(name = "version", length = 50)
    @get:Size(max = 50)
    var version: String?
        set(value): Unit = set(1, value)
        get(): String? = get(1) as String?

    @get:Column(name = "description", nullable = false, length = 200)
    @get:NotNull
    @get:Size(max = 200)
    var description: String?
        set(value): Unit = set(2, value)
        get(): String? = get(2) as String?

    @get:Column(name = "type", nullable = false, length = 20)
    @get:NotNull
    @get:Size(max = 20)
    var type: String?
        set(value): Unit = set(3, value)
        get(): String? = get(3) as String?

    @get:Column(name = "script", nullable = false, length = 1000)
    @get:NotNull
    @get:Size(max = 1000)
    var script: String?
        set(value): Unit = set(4, value)
        get(): String? = get(4) as String?

    @get:Column(name = "checksum", precision = 10)
    var checksum: Int?
        set(value): Unit = set(5, value)
        get(): Int? = get(5) as Int?

    @get:Column(name = "installed_by", nullable = false, length = 100)
    @get:NotNull
    @get:Size(max = 100)
    var installedBy: String?
        set(value): Unit = set(6, value)
        get(): String? = get(6) as String?

    @get:Column(name = "installed_on", nullable = false)
    @get:NotNull
    var installedOn: LocalDateTime?
        set(value): Unit = set(7, value)
        get(): LocalDateTime? = get(7) as LocalDateTime?

    @get:Column(name = "execution_time", nullable = false, precision = 10)
    @get:NotNull
    var executionTime: Int?
        set(value): Unit = set(8, value)
        get(): Int? = get(8) as Int?

    @get:Column(name = "success", nullable = false, precision = 3)
    @get:NotNull
    var success: Byte?
        set(value): Unit = set(9, value)
        get(): Byte? = get(9) as Byte?

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    override fun key(): Record1<Int?> = super.key() as Record1<Int?>

    // -------------------------------------------------------------------------
    // Record10 type implementation
    // -------------------------------------------------------------------------

    override fun fieldsRow(): Row10<Int?, String?, String?, String?, String?, Int?, String?, LocalDateTime?, Int?, Byte?> = super.fieldsRow() as Row10<Int?, String?, String?, String?, String?, Int?, String?, LocalDateTime?, Int?, Byte?>
    override fun valuesRow(): Row10<Int?, String?, String?, String?, String?, Int?, String?, LocalDateTime?, Int?, Byte?> = super.valuesRow() as Row10<Int?, String?, String?, String?, String?, Int?, String?, LocalDateTime?, Int?, Byte?>
    override fun field1(): Field<Int?> = FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY.INSTALLED_RANK
    override fun field2(): Field<String?> = FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY.VERSION
    override fun field3(): Field<String?> = FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY.DESCRIPTION
    override fun field4(): Field<String?> = FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY.TYPE
    override fun field5(): Field<String?> = FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY.SCRIPT
    override fun field6(): Field<Int?> = FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY.CHECKSUM
    override fun field7(): Field<String?> = FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY.INSTALLED_BY
    override fun field8(): Field<LocalDateTime?> = FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY.INSTALLED_ON
    override fun field9(): Field<Int?> = FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY.EXECUTION_TIME
    override fun field10(): Field<Byte?> = FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY.SUCCESS
    override fun component1(): Int? = installedRank
    override fun component2(): String? = version
    override fun component3(): String? = description
    override fun component4(): String? = type
    override fun component5(): String? = script
    override fun component6(): Int? = checksum
    override fun component7(): String? = installedBy
    override fun component8(): LocalDateTime? = installedOn
    override fun component9(): Int? = executionTime
    override fun component10(): Byte? = success
    override fun value1(): Int? = installedRank
    override fun value2(): String? = version
    override fun value3(): String? = description
    override fun value4(): String? = type
    override fun value5(): String? = script
    override fun value6(): Int? = checksum
    override fun value7(): String? = installedBy
    override fun value8(): LocalDateTime? = installedOn
    override fun value9(): Int? = executionTime
    override fun value10(): Byte? = success

    override fun value1(value: Int?): FlywaySchemaHistoryRecord {
        this.installedRank = value
        return this
    }

    override fun value2(value: String?): FlywaySchemaHistoryRecord {
        this.version = value
        return this
    }

    override fun value3(value: String?): FlywaySchemaHistoryRecord {
        this.description = value
        return this
    }

    override fun value4(value: String?): FlywaySchemaHistoryRecord {
        this.type = value
        return this
    }

    override fun value5(value: String?): FlywaySchemaHistoryRecord {
        this.script = value
        return this
    }

    override fun value6(value: Int?): FlywaySchemaHistoryRecord {
        this.checksum = value
        return this
    }

    override fun value7(value: String?): FlywaySchemaHistoryRecord {
        this.installedBy = value
        return this
    }

    override fun value8(value: LocalDateTime?): FlywaySchemaHistoryRecord {
        this.installedOn = value
        return this
    }

    override fun value9(value: Int?): FlywaySchemaHistoryRecord {
        this.executionTime = value
        return this
    }

    override fun value10(value: Byte?): FlywaySchemaHistoryRecord {
        this.success = value
        return this
    }

    override fun values(value1: Int?, value2: String?, value3: String?, value4: String?, value5: String?, value6: Int?, value7: String?, value8: LocalDateTime?, value9: Int?, value10: Byte?): FlywaySchemaHistoryRecord {
        this.value1(value1)
        this.value2(value2)
        this.value3(value3)
        this.value4(value4)
        this.value5(value5)
        this.value6(value6)
        this.value7(value7)
        this.value8(value8)
        this.value9(value9)
        this.value10(value10)
        return this
    }

    /**
     * Create a detached, initialised FlywaySchemaHistoryRecord
     */
    constructor(installedRank: Int? = null, version: String? = null, description: String? = null, type: String? = null, script: String? = null, checksum: Int? = null, installedBy: String? = null, installedOn: LocalDateTime? = null, executionTime: Int? = null, success: Byte? = null): this() {
        this.installedRank = installedRank
        this.version = version
        this.description = description
        this.type = type
        this.script = script
        this.checksum = checksum
        this.installedBy = installedBy
        this.installedOn = installedOn
        this.executionTime = executionTime
        this.success = success
    }

    /**
     * Create a detached, initialised FlywaySchemaHistoryRecord
     */
    constructor(value: com.mgt.earthquake.jooqmodel.tables.pojos.FlywaySchemaHistory?): this() {
        if (value != null) {
            this.installedRank = value.installedRank
            this.version = value.version
            this.description = value.description
            this.type = value.type
            this.script = value.script
            this.checksum = value.checksum
            this.installedBy = value.installedBy
            this.installedOn = value.installedOn
            this.executionTime = value.executionTime
            this.success = value.success
        }
    }
}
