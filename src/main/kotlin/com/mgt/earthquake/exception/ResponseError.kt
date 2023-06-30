package com.mgt.earthquake.exception

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.mgt.earthquake.util.Utils
import io.micronaut.core.annotation.Creator
import io.micronaut.core.annotation.Nullable
import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class ResponseError<T> @Creator @JsonCreator constructor(

    @param:JsonProperty("code") var code: Int = 0,

    @param:JsonProperty("time") var time: Long = 0,

    @param:JsonProperty("error") @field:Nullable var error: T
){


    @field:JsonProperty("stacktrace") @field:Nullable var stacktrace: String? = null

    constructor(code: Int, error: T) : this(code, Utils.currentTimeStampInMilis, error)

    constructor(code: Int, error: T, trace: String) : this(code, Utils.currentTimeStampInMilis, error){
        this.stacktrace = trace
    }
}