package com.mgt.earthquake.controller

import com.mgt.earthquake.model.QuakeDTO
import com.mgt.earthquake.model.QuakeDTOList
import com.mgt.earthquake.model.QuakeResponse
import com.mgt.earthquake.service.QuakeService
import com.mgt.earthquake.service.QuakeSqlService
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

@Controller
class QuakeController (
    private val quakeService: QuakeService,
    private val quakeSqlService: QuakeSqlService
) {

    @Get(value = "/test")
    suspend fun serverTest() = withContext(Dispatchers.IO) {
        "Hello world\n"
    }


    @Post(value = "/quake/add")
    suspend fun addQuakeItem(quake: QuakeDTO) = withContext(Dispatchers.IO) {

        quakeSqlService.create(quake)
        return@withContext quakeService.create(quake)
    }


    @Post(value = "/quake/addlist")
    suspend fun addQuakeList(quakelist: QuakeDTOList) = withContext(Dispatchers.IO) {

        quakeSqlService.createList(quakelist.quakeList)
        return@withContext quakeService.createList(quakelist.quakeList)
    }

    @Get(value = "/quake/latest")
    fun latestQuake(): Flow<QuakeResponse> = quakeService.latestQuake().flowOn(Dispatchers.IO)

}
