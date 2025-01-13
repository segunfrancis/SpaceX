package com.segunfrancis.spacex.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.segunfrancis.spacex.data.local.dto.CompanyInfo
import com.segunfrancis.spacex.data.local.dto.LaunchesItem

@Dao
interface SpaceXDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCompanyInfo(info: CompanyInfo)

    @Query("SELECT * FROM companyInfoTable")
    suspend fun getCompanyInfo(): CompanyInfo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLaunches(vararg launchesItem: LaunchesItem)

    @RawQuery(observedEntities = [LaunchesItem::class])
    suspend fun getLaunchesFromQuery(query: SupportSQLiteQuery): List<LaunchesItem>
}
