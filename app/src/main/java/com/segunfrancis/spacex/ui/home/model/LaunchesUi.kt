package com.segunfrancis.spacex.ui.home.model

data class LaunchesUi(
    val details: String?,
    val flightNumber: Int,
    val launchDateLocal: String,
    val launchDateUnix: Long,
    val launchDateUtc: String,
    val launchSuccess: Boolean?,
    val launchWindow: Int?,
    val launchYear: String,
    val missionName: String,
    val articleLink: String?,
    val missionPatch: String?,
    val missionPatchSmall: String?,
    val videoLink: String?,
    val wikipedia: String?,
    val youtubeId: String?,
    val rocketName: String,
    val rocketType: String,
    val onClick:() -> Unit
)
