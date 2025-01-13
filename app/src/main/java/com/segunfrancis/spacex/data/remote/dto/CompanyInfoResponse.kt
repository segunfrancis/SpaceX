package com.segunfrancis.spacex.data.remote.dto

import com.squareup.moshi.Json

data class CompanyInfoResponse(
    val ceo: String,
    val coo: String,
    val cto: String,
    @Json(name = "cto_propulsion")
    val ctoPropulsion: String,
    val employees: Int,
    val founded: Int,
    val founder: String,
    val headquarters: Headquarters,
    @Json(name = "launch_sites")
    val launchSites: Int,
    val name: String,
    val summary: String,
    @Json(name = "test_sites")
    val testSites: Int,
    val valuation: Long,
    val vehicles: Int
)
