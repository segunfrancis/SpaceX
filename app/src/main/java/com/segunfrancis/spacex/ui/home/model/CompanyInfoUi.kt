package com.segunfrancis.spacex.ui.home.model

import com.segunfrancis.spacex.data.local.dto.CompanyInfo

data class CompanyInfoUi(
    val companyName: String,
    val founderName: String,
    val year: Int,
    val launchSites: Int,
    val valuation: Long,
    val employees: Int
)

fun CompanyInfo.toCompanyInfoUi(): CompanyInfoUi {
    return CompanyInfoUi(
        companyName = name,
        founderName = founder,
        year = founded,
        launchSites = launchSites,
        valuation = valuation,
        employees = employees
    )
}
