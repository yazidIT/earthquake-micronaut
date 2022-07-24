package com.mgt.earthquake.controller

import com.mgt.earthquake.model.QuakeDTO
import com.mgt.earthquake.model.QuakeDTOList
import com.mgt.earthquake.model.QuakeResponse
import com.mgt.earthquake.service.QuakeService
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Controller
class QuakeController (
    private val quakeService: QuakeService
) {

    @Get(value = "/test")
    suspend fun serverTest() = withContext(Dispatchers.IO) {
        "Hello world\n"
    }


    @Post(value = "/quake/add")
    suspend fun addQuakeItem(quake: QuakeDTO) = withContext(Dispatchers.IO) {

        return@withContext quakeService.create(quake)
    }


    @Post(value = "/quake/addlist")
    suspend fun addQuakeList(quakelist: QuakeDTOList) = withContext(Dispatchers.IO) {

        return@withContext quakeService.createList(quakelist.quakeList)
    }

    @Get(value = "/quake/latest")
    suspend fun latestQuake(): QuakeResponse = withContext(Dispatchers.IO) {

        return@withContext quakeService.latestQuake()
    }
}
