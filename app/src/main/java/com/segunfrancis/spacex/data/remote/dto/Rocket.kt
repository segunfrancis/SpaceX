package com.segunfrancis.spacex.data.remote.dto

import com.squareup.moshi.Json

data class Rocket(
    @Json(name = "rocket_id")
    val rocketId: String,
    @Json(name = "rocket_name")
    val rocketName: String,
    @Json(name = "rocket_type")
    val rocketType: String
)
