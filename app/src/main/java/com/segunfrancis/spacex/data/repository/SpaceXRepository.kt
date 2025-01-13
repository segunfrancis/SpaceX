package com.segunfrancis.spacex.data.repository

import com.segunfrancis.spacex.data.local.dto.CompanyInfo
import com.segunfrancis.spacex.data.local.dto.LaunchesItem

interface SpaceXRepository {

    suspend fun getCompanyInfo(): CompanyInfo?

    suspend fun getLaunches(
        years: String? = null,
        launchState: Boolean? = null,
        order: String? = null
    ): List<LaunchesItem>
}
