package com.mgt.earthquake.controller

import com.mgt.earthquake.model.*
import com.mgt.earthquake.service.QuakeService
import com.mgt.earthquake.service.QuakeSqlService
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory

@Controller
class QuakeController (
    private val quakeService: QuakeService,
    private val quakeSqlService: QuakeSqlService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    @Get(value = "/test")
    suspend fun serverTest() = withContext(ioDispatcher) {
        "Hello world\n"
    }


    @Post(value = "/quake/add")
    suspend fun addQuakeItem(quake: QuakeDTO) = withContext(ioDispatcher) {

        quakeSqlService.create(quake)
        quakeService.create(quake)
    }


    @Post(value = "/quake/addlist")
    fun addQuakeList(quakelist: QuakeDTOList) = flow<QuakeModel> {

        quakeSqlService.createList(quakelist.quakeList)
        quakeService.createList(quakelist.quakeList)
    }.flowOn(ioDispatcher)


    @Get(value = "/quake/latest")
    fun latestQuake(): Flow<QuakeResponse> = quakeService.latestQuake().flowOn(ioDispatcher)


    @Get(value = "/quake/list/json/{number}", produces = [MediaType.APPLICATION_JSON_STREAM])
    fun latestListJsonByNumber(@PathVariable number: Int): Flow<QuakeModelDTO> =

        quakeService.latestNumberOfQuake(number).flowOn(ioDispatcher)


    @Get(value = "/quake/list/{number}")
    fun latestListByNumber(@PathVariable number: Int): Flow<QuakeModelDTO> =

        quakeService.latestNumberOfQuake(number).flowOn(ioDispatcher)


    @Get(value = "/quake/stream/json/{number}", produces = [MediaType.TEXT_EVENT_STREAM])
    fun latestStreamJsonByNumber(@PathVariable number: Int): Flow<QuakeModelDTO> =

        quakeService.latestNumberOfQuake(number).flowOn(ioDispatcher)
}
