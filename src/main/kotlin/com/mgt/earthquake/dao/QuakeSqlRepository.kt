package com.mgt.earthquake.dao

import com.mgt.earthquake.jooqmodel.tables.daos.QuakeDao
import com.mgt.earthquake.jooqmodel.tables.pojos.Quake
import com.mgt.earthquake.jooqmodel.tables.records.QuakeRecord
import com.mgt.earthquake.jooqmodel.tables.references.QUAKE
import io.micronaut.data.r2dbc.annotation.R2dbcRepository
import io.micronaut.transaction.annotation.ReadOnly
import io.micronaut.transaction.annotation.TransactionalAdvice
import jakarta.inject.Named
import org.jooq.Configuration
import org.jooq.DSLContext
import org.slf4j.LoggerFactory
import javax.transaction.Transactional

@R2dbcRepository(value = "default")
@TransactionalAdvice("default")
class QuakeSqlRepository (
    private val dslContext: DSLContext,
    @Named("CustomJooqConfig") private val configuration: Configuration,
) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val quakeDao = QuakeDao(configuration)


    @ReadOnly
    fun findAll(): List<Quake> {

        return runCatching {
            dslContext.selectFrom(QUAKE)
                .fetchInto(Quake::class.java)

        }.getOrElse {
            logger.error(it.message)
            emptyList()
        }
    }


    @ReadOnly
    fun findById(id: Long): Quake? = quakeDao.fetchOneById(id)

    @ReadOnly
    fun findByQuakeId(quakeid: String): Quake? = quakeDao.fetchOne(QUAKE.QUAKEID, quakeid)


    @Transactional
    fun create(quakePojo: Quake): QuakeRecord {

        val createdquake = dslContext.newRecord(QUAKE)
        createdquake.apply {
            title = quakePojo.title
            magnitude = quakePojo.magnitude
            quaketime = quakePojo.quaketime
            latitude = quakePojo.latitude
            longitude = quakePojo.longitude
            quakeid = quakePojo.quakeid
        }

        createdquake.store()
        return createdquake
    }

    @Transactional
    fun update(quakePojo: Quake) = quakeDao.update(quakePojo)


    @Transactional
    fun createQuakes(quakePojos : List<Quake>) = quakeDao.insert(quakePojos)


    @Transactional
    fun delete(id: Long) = quakeDao.fetchOneById(id)?.let { quakeDao.delete(it) }


    @Transactional
    fun delete(ids: List<Long>) = quakeDao.deleteById(ids)


    @Transactional
    fun deleteAll() {

        val allIds = findAll().map { it.id }
        quakeDao.deleteById(allIds)
    }
}