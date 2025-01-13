package com.segunfrancis.spacex.usecase

import com.segunfrancis.spacex.data.repository.SpaceXRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.supervisorScope

class HomeUseCase(
    private val repository: SpaceXRepository,
    private val dispatcher: CoroutineDispatcher
) {

    operator fun invoke(
        years: String? = null,
        launchState: Boolean? = null,
        order: String? = null
    ): Flow<HomeResponse> {
        var homeResponse = HomeResponse()
        return flow {
            supervisorScope {
                val companyInfoDef = async { repository.getCompanyInfo() }
                val launchesDef = async { repository.getLaunches(years, launchState, order) }
                try {
                    homeResponse = homeResponse.copy(info = companyInfoDef.await())
                    emit(homeResponse)
                } catch (t: Throwable) {
                    homeResponse = homeResponse.copy(infoError = t)
                    emit(homeResponse)
                }
                try {
                    homeResponse = homeResponse.copy(launches = launchesDef.await())
                    emit(homeResponse)
                } catch (t: Throwable) {
                    homeResponse = homeResponse.copy(launchesError = t)
                    emit(homeResponse)
                }
            }
        }.flowOn(dispatcher)
    }
}
