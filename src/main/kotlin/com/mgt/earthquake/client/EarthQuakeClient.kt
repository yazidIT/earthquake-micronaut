package com.mgt.earthquake.client

import com.mgt.earthquake.model.QuakeResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client
import kotlinx.coroutines.flow.Flow

@Client(value = "EmailSender", path = "/fdsnws", id = "\${client.earthquake.url}")
interface EarthQuakeClient {

    @Get(value = "/event/1/query", consumes = [MediaType.APPLICATION_JSON])
    fun getTopEarthquakeForToday(
        @QueryValue format: String,
        @QueryValue limit: Int,
        @QueryValue minmagnitude: Double,
        @QueryValue maxmagnitude: Double,
        @QueryValue starttime: String,
        @QueryValue endtime: String,
        @QueryValue orderby: String,
    ): Flow<QuakeResponse>
}