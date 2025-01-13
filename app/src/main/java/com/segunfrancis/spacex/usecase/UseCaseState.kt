package com.segunfrancis.spacex.usecase

import com.segunfrancis.spacex.data.local.dto.CompanyInfo
import com.segunfrancis.spacex.data.local.dto.LaunchesItem

data class HomeResponse(
    val info: CompanyInfo? = null,
    val launches: List<LaunchesItem>? = null,
    val infoError: Throwable? = null,
    val launchesError: Throwable? = null
)
