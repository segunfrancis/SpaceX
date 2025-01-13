package com.segunfrancis.spacex.data.remote

import com.segunfrancis.spacex.data.remote.dto.CompanyInfoResponse
import com.segunfrancis.spacex.data.remote.dto.LaunchesResponseItem
import retrofit2.http.GET

interface SpaceXApi {

    @GET("info")
    suspend fun getCompanyInfo() : CompanyInfoResponse

    @GET("launches")
    suspend fun getAllLaunches() : List<LaunchesResponseItem>
}
