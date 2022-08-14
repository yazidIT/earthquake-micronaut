package com.mgt.earthquake.dao

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
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import javax.transaction.Transactional

@R2dbcRepository(value = "default")
@TransactionalAdvice("default")
class QuakeSqlRepository (
    @Named("R2dbcJooqDslContext") private val dslContext: DSLContext,
    @Named("CustomJooqConfig") private val configuration: Configuration,
) {

    private val logger = LoggerFactory.getLogger(this::class.java)


    @ReadOnly
    fun findAll(): List<Quake> {

        return runCatching {
            Flux.from(
                dslContext.selectFrom(QUAKE))
                .map { it.into(Quake::class.java) }
                .toStream()
                .toList()

        }.getOrElse {
            logger.error(it.message)
            emptyList()
        }
    }


    @ReadOnly
    fun findById(id: Long): Quake? {

        return Mono.from(
            dslContext.selectFrom(QUAKE)
                .where(QUAKE.ID.eq(id)))
            .map { it.into(Quake::class.java) }
            .block()
    }

    @ReadOnly
    fun findByQuakeId(quakeid: String): Quake? {

        return Mono.from(
            dslContext.selectFrom(QUAKE)
                .where(QUAKE.QUAKEID.eq(quakeid)))
            .map { it.into(Quake::class.java) }
            .block()
    }


    @Transactional
    fun create(quakePojo: Quake): QuakeRecord? {

        val createdquake = dslContext.newRecord(QUAKE)
        createdquake.from(quakePojo)

        val result = Mono.from(
            dslContext.insertInto(QUAKE)
                .set(createdquake)
                .returningResult(QUAKE.ID))
            .block()

        return createdquake.apply { id = result!!.value1() }
    }

    @Transactional
    fun update(quakePojo: Quake) {

        val createdquake = dslContext.newRecord(QUAKE)
        createdquake.from(quakePojo)

        Mono.from(
            dslContext.update(QUAKE)
                .set(createdquake)
                .where(QUAKE.ID.eq(quakePojo.id))
                .returningResult(QUAKE.ID))
            .block()
    }


    @Transactional
    fun createQuakes(quakePojos : List<Quake>) = quakePojos.map { create(it) }


    @Transactional
    fun delete(id: Long) {
        Mono.from(
            dslContext.deleteFrom(QUAKE)
                .where(QUAKE.ID.eq(id)))
            .block()
    }


    @Transactional
    fun delete(ids: List<Long>) = ids.map { delete(it) }


    @Transactional
    fun deleteAll() {

        Mono.from(
            dslContext.deleteFrom(QUAKE)
        ).block()
    }
}