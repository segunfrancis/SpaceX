package com.segunfrancis.spacex.data.remote.dto

import com.squareup.moshi.Json

data class Links(
    @Json(name = "article_link")
    val articleLink: String?,
    @Json(name = "mission_patch")
    val missionPatch: String?,
    @Json(name = "mission_patch_small")
    val missionPatchSmall: String?,
    @Json(name = "video_link")
    val videoLink: String?,
    val wikipedia: String?,
    @Json(name = "youtube_id")
    val youtubeId: String?
)
