package com.mgt.earthquake.util

import org.slf4j.LoggerFactory
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

object Utils {

    private val logger = LoggerFactory.getLogger(this::class.java)

    val currentTimeStamp: String
        get() {
            val isoFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val dateCurrent = Calendar.getInstance().time
            return isoFormat.format(dateCurrent)
        }

    val currentTimeStampInMilis: Long
        get() = Instant.now().toEpochMilli()

    fun timeStampFormat(timestamp: Long): String {
        val isoFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z")
        return isoFormat.format(timestamp)
    }
}