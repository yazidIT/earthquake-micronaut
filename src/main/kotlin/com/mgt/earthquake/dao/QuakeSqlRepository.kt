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
import org.jooq.impl.DSL.row
import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import javax.transaction.Transactional

@R2dbcRepository(value = "default")
@TransactionalAdvice("default")
class QuakeSqlRepository (
    @Named("R2dbcJooqDslContext") private val dslContext: DSLContext,
) {

    private val logger = LoggerFactory.getLogger(this::class.java)


    @ReadOnly
    fun findAll(): List<Quake> {

        return Flux.from(
            dslContext.selectFrom(QUAKE))
            .map { it.into(Quake::class.java) }
            .toStream()
            .toList()
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

        with(QUAKE) {
            return Mono.from(
                dslContext.insertInto(this, LATITUDE, LONGITUDE, MAGNITUDE, QUAKEID, QUAKETIME, TITLE)
                    .values(quakePojo.latitude, quakePojo.longitude, quakePojo.magnitude,
                        quakePojo.quakeid, quakePojo.quaketime, quakePojo.title)
                    .returningResult(QUAKE.ID))
                .flatMap{
                    Mono.from(
                        dslContext.selectFrom(this)
                            .where(ID.eq(it[ID]))
                    )
                }
                .block()
        }
    }


    @Transactional
    fun update(quakePojo: Quake): QuakeRecord? {

        with(QUAKE) {
            return Mono.from(
                dslContext.update(QUAKE)
                    .set(
                        row(LATITUDE, LONGITUDE, MAGNITUDE, QUAKEID, QUAKETIME, TITLE),
                        row(
                            quakePojo.latitude, quakePojo.longitude, quakePojo.magnitude,
                            quakePojo.quakeid, quakePojo.quaketime, quakePojo.title
                        )
                    )
                    .where(ID.eq(quakePojo.id))
                    .returningResult(ID))
                .flatMap {
                    Mono.from(
                        dslContext.selectFrom(this)
                            .where(ID.eq(it[ID]))
                    )
                }
                .block()
        }
    }


    @Transactional
    fun createQuakes(quakePojos : List<Quake>) = quakePojos.map { create(it) }


    @Transactional
    fun delete(id: Long) = delete(listOf(id))


    @Transactional
    fun delete(ids: List<Long>) {

        Mono.from(
            dslContext.deleteFrom(QUAKE).where(QUAKE.ID.`in`(ids)))
            .block()
    }


    @Transactional
    fun deleteAll() {

        Mono.from(
            dslContext.deleteFrom(QUAKE))
            .block()
    }
}