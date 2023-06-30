package com.mgt.earthquake.exception

import com.mgt.earthquake.util.Utils
import io.micronaut.core.annotation.Creator
import io.micronaut.core.annotation.Nullable
import io.micronaut.serde.annotation.Serdeable
import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonProperty

@Serdeable
data class ResponseError<T> @Creator @BsonCreator constructor(

    @param:BsonProperty("code") var code: Int = 0,

    @param:BsonProperty("time") var time: Long = 0,

    @param:BsonProperty("error") @field:Nullable var error: T
){


    @field:BsonProperty("stacktrace") @field:Nullable var stacktrace: String? = null

    constructor(code: Int, error: T) : this(code, Utils.currentTimeStampInMilis, error)

    constructor(code: Int, error: T, trace: String) : this(code, Utils.currentTimeStampInMilis, error){
        this.stacktrace = trace
    }
}