package com.mgt.earthquake.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity(name = "quake")
class QuakeSqlModel(

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
