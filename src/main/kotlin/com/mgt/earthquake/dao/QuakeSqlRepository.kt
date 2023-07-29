package com.mgt.earthquake.dao

import com.mgt.earthquake.jooqmodel.tables.pojos.Quake
import com.mgt.earthquake.jooqmodel.tables.records.QuakeRecord
import com.mgt.earthquake.jooqmodel.tables.references.QUAKE
import io.micronaut.data.r2dbc.annotation.R2dbcRepository
import io.micronaut.transaction.annotation.ReadOnly
import io.micronaut.transaction.annotation.TransactionalAdvice
import jakarta.inject.Named
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitLast
import org.jooq.DSLContext
import org.jooq.impl.DSL.row
import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import javax.transaction.Transactional

@R2dbcRepository(value = "rx-quake")
@TransactionalAdvice("rx-quake")
class QuakeSqlRepository (
    @Named("R2dbcJooqDslContext") private val dslContext: DSLContext,
) {

    private val logger = LoggerFactory.getLogger(this::class.java)


    @ReadOnly
    fun findAll() =

        Flux.from(dslContext.selectFrom(QUAKE))
            .map { it.into(Quake::class.java) }
            .asFlow()



    @ReadOnly
    suspend fun findById(id: Long) =

        Mono.from(dslContext.selectFrom(QUAKE).where(QUAKE.ID.eq(id)))
            .map { it.into(Quake::class.java) }
            .awaitFirstOrNull()


    @ReadOnly
    suspend fun findByQuakeId(quakeid: String) =

        Mono.from(dslContext.selectFrom(QUAKE).where(QUAKE.QUAKEID.eq(quakeid)))
            .map { it.into(Quake::class.java) }
            .awaitFirstOrNull()


    @Transactional
    suspend fun create(quakePojo: Quake): QuakeRecord? =

        with(QUAKE) {
            Mono.from(
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
                .awaitFirstOrNull()
        }


    @Transactional
    suspend fun update(quakePojo: Quake): QuakeRecord? =

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
                .awaitFirstOrNull()
        }


    @Transactional
    suspend fun createQuakes(quakePojos : List<Quake>) = quakePojos.map { create(it) }


    @Transactional
    suspend fun delete(id: Long) = delete(listOf(id))


    @Transactional
    suspend fun delete(ids: List<Long>) =

        Mono.from(dslContext.deleteFrom(QUAKE).where(QUAKE.ID.`in`(ids))).awaitLast()


    @Transactional
    suspend fun deleteAll() = Mono.from(dslContext.deleteFrom(QUAKE)).awaitLast()
}