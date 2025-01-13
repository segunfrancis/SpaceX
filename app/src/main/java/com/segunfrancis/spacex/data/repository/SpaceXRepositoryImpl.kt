package com.segunfrancis.spacex.data.repository

import androidx.sqlite.db.SimpleSQLiteQuery
import com.segunfrancis.spacex.data.local.SpaceXDao
import com.segunfrancis.spacex.data.local.dto.CompanyInfo
import com.segunfrancis.spacex.data.local.dto.LaunchesItem
import com.segunfrancis.spacex.data.remote.SpaceXApi
import com.segunfrancis.spacex.data.toCompanyInfoRemote
import com.segunfrancis.spacex.data.toLaunchesLocal
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class SpaceXRepositoryImpl(
    private val dao: SpaceXDao,
    private val api: SpaceXApi,
    private val dispatcher: CoroutineDispatcher
) : SpaceXRepository {
    override suspend fun getCompanyInfo(): CompanyInfo? {
        val localResponse = withContext(dispatcher) { dao.getCompanyInfo() }
        return if (localResponse != null) {
            localResponse
        } else {
            val remoteResponse = api.getCompanyInfo()
            dao.addCompanyInfo(remoteResponse.toCompanyInfoRemote())
            withContext(dispatcher) { dao.getCompanyInfo() }
        }
    }

    override suspend fun getLaunches(
        years: String?,
        launchState: Boolean?,
        order: String?
    ): List<LaunchesItem> {
        val localLaunches = withContext(dispatcher) {
            dao.getLaunchesFromQuery(
                buildFilterQuery(
                    years,
                    launchState,
                    order
                )
            )
        }
        return localLaunches.ifEmpty {
            val remoteResponse = api.getAllLaunches()
            val launchesItem = remoteResponse.map { response ->
                response.toLaunchesLocal()
            }
            withContext(dispatcher) { dao.addLaunches(*launchesItem.toTypedArray()) }
            val updatedLocalResponse = withContext(dispatcher) {
                dao.getLaunchesFromQuery(
                    buildFilterQuery(
                        years,
                        launchState,
                        order
                    )
                )
            }
            updatedLocalResponse
        }
    }

    private fun buildFilterQuery(
        years: String?,
        launchState: Boolean?,
        order: String?
    ): SimpleSQLiteQuery {
        var containsCondition = false
        val queryStringBuilder = StringBuilder()
        val args: MutableList<Any> = ArrayList()
        queryStringBuilder.append("SELECT * FROM launchesTable")
        years?.let {
            queryStringBuilder.append(" WHERE launchYear = ?")
            args.add(it)
            containsCondition = true
        }
        launchState?.let {
            if (containsCondition) {
                queryStringBuilder.append(" AND")
            } else {
                queryStringBuilder.append(" WHERE")
                containsCondition = true
            }
            queryStringBuilder.append(" launchSuccess = ?")
            args.add(launchState)
        }
        order?.let {
            if (it.equals("descending", true))
                queryStringBuilder.append(" ORDER BY launchDateUnix DESC")
            else
                queryStringBuilder.append(" ORDER BY launchDateUnix ASC")
        }
        queryStringBuilder.append(";")
        return SimpleSQLiteQuery(queryStringBuilder.toString(), args.toTypedArray())
    }
}
