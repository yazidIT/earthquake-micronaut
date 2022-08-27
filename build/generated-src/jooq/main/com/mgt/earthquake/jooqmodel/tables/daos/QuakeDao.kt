/*
 * This file is generated by jOOQ.
 */
package com.mgt.earthquake.jooqmodel.tables.daos


import com.mgt.earthquake.jooqmodel.tables.Quake
import com.mgt.earthquake.jooqmodel.tables.records.QuakeRecord

import kotlin.collections.List

import org.jooq.Configuration
import org.jooq.impl.DAOImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class QuakeDao(configuration: Configuration?) : DAOImpl<QuakeRecord, com.mgt.earthquake.jooqmodel.tables.pojos.Quake, Long>(Quake.QUAKE, com.mgt.earthquake.jooqmodel.tables.pojos.Quake::class.java, configuration) {

    /**
     * Create a new QuakeDao without any configuration
     */
    constructor(): this(null)

    override fun getId(o: com.mgt.earthquake.jooqmodel.tables.pojos.Quake): Long? = o.id

    /**
     * Fetch records that have <code>id BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    fun fetchRangeOfId(lowerInclusive: Long?, upperInclusive: Long?): List<com.mgt.earthquake.jooqmodel.tables.pojos.Quake> = fetchRange(Quake.QUAKE.ID, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>id IN (values)</code>
     */
    fun fetchById(vararg values: Long): List<com.mgt.earthquake.jooqmodel.tables.pojos.Quake> = fetch(Quake.QUAKE.ID, *values.toTypedArray())

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    fun fetchOneById(value: Long): com.mgt.earthquake.jooqmodel.tables.pojos.Quake? = fetchOne(Quake.QUAKE.ID, value)

    /**
     * Fetch records that have <code>latitude BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    fun fetchRangeOfLatitude(lowerInclusive: Double?, upperInclusive: Double?): List<com.mgt.earthquake.jooqmodel.tables.pojos.Quake> = fetchRange(Quake.QUAKE.LATITUDE, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>latitude IN (values)</code>
     */
    fun fetchByLatitude(vararg values: Double): List<com.mgt.earthquake.jooqmodel.tables.pojos.Quake> = fetch(Quake.QUAKE.LATITUDE, *values.toTypedArray())

    /**
     * Fetch records that have <code>longitude BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    fun fetchRangeOfLongitude(lowerInclusive: Double?, upperInclusive: Double?): List<com.mgt.earthquake.jooqmodel.tables.pojos.Quake> = fetchRange(Quake.QUAKE.LONGITUDE, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>longitude IN (values)</code>
     */
    fun fetchByLongitude(vararg values: Double): List<com.mgt.earthquake.jooqmodel.tables.pojos.Quake> = fetch(Quake.QUAKE.LONGITUDE, *values.toTypedArray())

    /**
     * Fetch records that have <code>magnitude BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    fun fetchRangeOfMagnitude(lowerInclusive: Double?, upperInclusive: Double?): List<com.mgt.earthquake.jooqmodel.tables.pojos.Quake> = fetchRange(Quake.QUAKE.MAGNITUDE, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>magnitude IN (values)</code>
     */
    fun fetchByMagnitude(vararg values: Double): List<com.mgt.earthquake.jooqmodel.tables.pojos.Quake> = fetch(Quake.QUAKE.MAGNITUDE, *values.toTypedArray())

    /**
     * Fetch records that have <code>quakeid BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    fun fetchRangeOfQuakeid(lowerInclusive: String?, upperInclusive: String?): List<com.mgt.earthquake.jooqmodel.tables.pojos.Quake> = fetchRange(Quake.QUAKE.QUAKEID, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>quakeid IN (values)</code>
     */
    fun fetchByQuakeid(vararg values: String): List<com.mgt.earthquake.jooqmodel.tables.pojos.Quake> = fetch(Quake.QUAKE.QUAKEID, *values)

    /**
     * Fetch a unique record that has <code>quakeid = value</code>
     */
    fun fetchOneByQuakeid(value: String): com.mgt.earthquake.jooqmodel.tables.pojos.Quake? = fetchOne(Quake.QUAKE.QUAKEID, value)

    /**
     * Fetch records that have <code>quaketime BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    fun fetchRangeOfQuaketime(lowerInclusive: String?, upperInclusive: String?): List<com.mgt.earthquake.jooqmodel.tables.pojos.Quake> = fetchRange(Quake.QUAKE.QUAKETIME, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>quaketime IN (values)</code>
     */
    fun fetchByQuaketime(vararg values: String): List<com.mgt.earthquake.jooqmodel.tables.pojos.Quake> = fetch(Quake.QUAKE.QUAKETIME, *values)

    /**
     * Fetch records that have <code>title BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    fun fetchRangeOfTitle(lowerInclusive: String?, upperInclusive: String?): List<com.mgt.earthquake.jooqmodel.tables.pojos.Quake> = fetchRange(Quake.QUAKE.TITLE, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>title IN (values)</code>
     */
    fun fetchByTitle(vararg values: String): List<com.mgt.earthquake.jooqmodel.tables.pojos.Quake> = fetch(Quake.QUAKE.TITLE, *values)
}
