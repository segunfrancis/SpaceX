package com.segunfrancis.spacex.data

import com.segunfrancis.spacex.data.local.dto.CompanyInfo
import com.segunfrancis.spacex.data.local.dto.LaunchesItem
import com.segunfrancis.spacex.data.local.dto.Links
import com.segunfrancis.spacex.data.local.dto.Rocket
import com.segunfrancis.spacex.data.remote.dto.CompanyInfoResponse
import com.segunfrancis.spacex.data.remote.dto.LaunchesResponseItem

fun CompanyInfoResponse.toCompanyInfoRemote(): CompanyInfo {
    return CompanyInfo(
        ceo = ceo,
        coo = coo,
        cto = cto,
        ctoPropulsion = ctoPropulsion,
        employees = employees,
        founded = founded,
        founder = founder,
        launchSites = launchSites,
        name = name,
        summary = summary,
        testSites = testSites,
        vehicles = vehicles,
        valuation = valuation
    )
}

fun LaunchesResponseItem.toLaunchesLocal(): LaunchesItem {
    return LaunchesItem(
        details = details,
        flightNumber = flightNumber,
        isTentative = isTentative,
        launchSuccess = launchSuccess,
        launchDateUnix = launchDateUnix,
        launchDateLocal = launchDateLocal,
        launchDateUtc = launchDateUtc,
        launchWindow = launchWindow,
        launchYear = launchYear,
        missionName = missionName,
        tbd = tbd,
        upcoming = upcoming,
        links = links?.toLinksLocal(),
        rocket = rocket.toRocketLocal()
    )
}

private fun com.segunfrancis.spacex.data.remote.dto.Rocket.toRocketLocal(): Rocket {
    return Rocket(rocketId, rocketName, rocketType)
}

private fun com.segunfrancis.spacex.data.remote.dto.Links.toLinksLocal(): Links {
    return Links(
        articleLink = articleLink,
        missionPatch = missionPatch,
        missionPatchSmall = missionPatchSmall,
        videoLink = videoLink,
        wikipedia = wikipedia,
        youtubeId = youtubeId
    )
}
