package com.mgt.earthquake.controller

import com.mgt.earthquake.model.QuakeDTO
import com.mgt.earthquake.model.QuakeDTOList
import com.mgt.earthquake.model.QuakeModel
import com.mgt.earthquake.model.QuakeResponse
import com.mgt.earthquake.service.QuakeService
import com.mgt.earthquake.service.QuakeSqlService
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext

@Controller
class QuakeController (
    private val quakeService: QuakeService,
    private val quakeSqlService: QuakeSqlService
) {

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    @Get(value = "/test")
    suspend fun serverTest() = withContext(ioDispatcher) {
        "Hello world\n"
    }


    @Post(value = "/quake/add")
    suspend fun addQuakeItem(quake: QuakeDTO) = withContext(ioDispatcher) {

        quakeSqlService.create(quake)
        return@withContext quakeService.create(quake)
    }


    @Post(value = "/quake/addlist")
    suspend fun addQuakeList(quakelist: QuakeDTOList) = withContext(ioDispatcher) {

        quakeSqlService.createList(quakelist.quakeList)
        return@withContext quakeService.createList(quakelist.quakeList).toList()
    }


    @Get(value = "/quake/latest")
    fun latestQuake(): Flow<QuakeResponse> = quakeService.latestQuake().flowOn(ioDispatcher)


    @Get(value = "/quake/list/json/{number}", produces = [MediaType.APPLICATION_JSON_STREAM])
    fun latestListJsonByNumber(@PathVariable number: Int): Flow<QuakeModel> =

        quakeService.latestNumberOfQuake(number).flowOn(ioDispatcher)


    @Get(value = "/quake/list/{number}")
    fun latestListByNumber(@PathVariable number: Int): Flow<QuakeModel> =

        quakeService.latestNumberOfQuake(number).flowOn(ioDispatcher)
}
