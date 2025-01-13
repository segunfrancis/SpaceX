package com.segunfrancis.spacex.data.local.dto

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "launchesTable")
data class LaunchesItem(
    val details: String?,
    val flightNumber: Int,
    val isTentative: Boolean,
    val launchDateLocal: String,
    @PrimaryKey(autoGenerate = false)
    val launchDateUnix: Long,
    val launchDateUtc: String,
    val launchSuccess: Boolean?,
    val launchWindow: Int?,
    val launchYear: String,
    @Embedded val links: Links?,
    val missionName: String,
    @Embedded val rocket: Rocket,
    val tbd: Boolean,
    val upcoming: Boolean
)

data class Links(
    val articleLink: String?,
    val missionPatch: String?,
    val missionPatchSmall: String?,
    val wikipedia: String?,
    val videoLink: String?,
    val youtubeId: String?
)

data class Rocket(
    val rocketId: String,
    val rocketName: String,
    val rocketType: String
)
