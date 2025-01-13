package com.segunfrancis.spacex.data.remote.dto

import com.squareup.moshi.Json

data class LaunchesResponseItem(
    val details: String?,
    @Json(name = "flight_number")
    val flightNumber: Int,
    @Json(name = "is_tentative")
    val isTentative: Boolean,
    @Json(name = "launch_date_local")
    val launchDateLocal: String,
    @Json(name = "launch_date_unix")
    val launchDateUnix: Long,
    @Json(name = "launch_date_utc")
    val launchDateUtc: String,
    @Json(name = "launch_success")
    val launchSuccess: Boolean?,
    @Json(name = "launch_window")
    val launchWindow: Int?,
    @Json(name = "launch_year")
    val launchYear: String,
    val links: Links?,
    @Json(name = "mission_name")
    val missionName: String,
    val rocket: Rocket,
    val tbd: Boolean,
    val upcoming: Boolean
)
