package com.mgt.earthquake.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity(name = "quake")
data class QuakeSqlModel(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var title: String,
    var magnitude: Double,
    var quaketime: String,
    var latitude: Double,
    var longitude: Double,
    var quakeid: String
)
