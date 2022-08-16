package com.mgt.earthquake.dao

import io.micronaut.context.annotation.ConfigurationProperties

@ConfigurationProperties("r2dbc.datasources.rx-quake")
class R2dbcConfiguration {
    var url: String? = null
    var username: String? = null
    var password: String? = null
}