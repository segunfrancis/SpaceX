package com.segunfrancis.spacex.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.segunfrancis.spacex.data.local.dto.LaunchesItem
import com.segunfrancis.spacex.ui.home.model.CompanyInfoUi
import com.segunfrancis.spacex.ui.home.model.LaunchesUi
import com.segunfrancis.spacex.ui.home.model.toCompanyInfoUi
import com.segunfrancis.spacex.usecase.HomeUseCase
import com.segunfrancis.spacex.util.handleThrowable
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(private val useCase: HomeUseCase) : ViewModel() {

    private val _updatedUiState = MutableStateFlow(HomeUiStateUpdated())
    val updatedUiState: StateFlow<HomeUiStateUpdated> get() = _updatedUiState

    private val _interaction = MutableSharedFlow<HomeActions>()
    val interactions: SharedFlow<HomeActions> get() = _interaction

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _updatedUiState.update { it.copy(errors = listOf(throwable.handleThrowable())) }
    }

    private val _chipUiState = MutableStateFlow(ChipUiState())
    val chipState = _chipUiState.asStateFlow()

    private val years = mutableSetOf<String>()

    init {
        getHomeScreenDateUpdated()
    }

    fun getHomeScreenDateUpdated() {
        _updatedUiState.update { it.copy(isLoading = true) }
        viewModelScope.launch(exceptionHandler) {
            useCase.invoke().collectLatest { response ->
                _updatedUiState.update { state ->
                    response.launches?.map { it.launchYear }?.let { years.addAll(it) }
                    state.copy(
                        info = response.info?.toCompanyInfoUi(),
                        launches = response.launches?.map {
                            it.toLaunchUi()
                        } ?: emptyList(),
                        errors = listOf(
                            response.infoError?.handleThrowable(),
                            response.launchesError?.handleThrowable()
                        ),
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun LaunchesItem.toLaunchUi(): LaunchesUi {
        return LaunchesUi(
            details = details,
            flightNumber = flightNumber,
            launchDateLocal = launchDateLocal,
            launchDateUtc = launchDateUtc,
            launchDateUnix = launchDateUnix,
            launchWindow = launchWindow,
            launchSuccess = launchSuccess,
            launchYear = launchYear,
            missionName = missionName,
            articleLink = links?.articleLink,
            missionPatchSmall = links?.missionPatchSmall,
            missionPatch = links?.missionPatch,
            videoLink = links?.videoLink,
            wikipedia = links?.wikipedia,
            youtubeId = links?.youtubeId,
            rocketName = rocket.rocketName,
            rocketType = rocket.rocketType,
            onClick = {
                viewModelScope.launch {
                    _interaction.emit(
                        HomeActions.Navigate(
                            HomeFragmentDirections.toViewMoreFragment(
                                articleLink = links?.articleLink,
                                wikiLink = links?.wikipedia,
                                videoLink = links?.videoLink
                            )
                        )
                    )
                }
            }
        )
    }

    fun onMenuItemClicked() {
        viewModelScope.launch(exceptionHandler) {
            if (years.isNotEmpty()) {
                _interaction.emit(HomeActions.Navigate(HomeFragmentDirections.toFilterFragment(years = years.toTypedArray())))
            }
        }
    }

    fun setFilters(year: String?, launchState: Boolean?, order: String?) {
        viewModelScope.launch(exceptionHandler) {
            useCase.invoke(year, launchState, order).collect { response ->
                _updatedUiState.update { state ->
                    state.copy(
                        info = response.info?.toCompanyInfoUi(),
                        launches = response.launches?.map {
                            it.toLaunchUi()
                        } ?: emptyList(),
                        errors = listOf(
                            response.infoError?.handleThrowable(),
                            response.launchesError?.handleThrowable()
                        ),
                        isLoading = false
                    )
                }
            }
        }
    }

    fun updateYearsId(id: Int) {
        _chipUiState.update { it.copy(yearsId = id) }
    }

    fun updateLaunchStateId(id: Int) {
        _chipUiState.update { it.copy(launchStateId = id) }
    }

    fun updateOrderId(id: Int) {
        _chipUiState.update { it.copy(orderId = id) }
    }

    fun resetAllChipIds() {
        _chipUiState.update { it.copy(yearsId = -1, launchStateId = -1, orderId = -1) }
    }

    data class HomeUiStateUpdated(
        val info: CompanyInfoUi? = null,
        val launches: List<LaunchesUi> = emptyList(),
        val errors: List<String?> = emptyList(),
        val isLoading: Boolean = false
    )

    sealed class HomeActions {
        data class Navigate(val navDirections: NavDirections) : HomeActions()
    }

    data class ChipUiState(
        val yearsId: Int = -1,
        val launchStateId: Int = -1,
        val orderId: Int = -1
    )
}
