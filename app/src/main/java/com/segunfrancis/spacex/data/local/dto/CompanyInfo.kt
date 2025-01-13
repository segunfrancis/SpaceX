package com.segunfrancis.spacex.data.local.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "companyInfoTable")
data class CompanyInfo(
    val ceo: String,
    val coo: String,
    val cto: String,
    val ctoPropulsion: String,
    val employees: Int,
    val founded: Int,
    val founder: String,
    val launchSites: Int,
    @PrimaryKey(autoGenerate = false)
    val name: String,
    val summary: String,
    val testSites: Int,
    val valuation: Long,
    val vehicles: Int
)
