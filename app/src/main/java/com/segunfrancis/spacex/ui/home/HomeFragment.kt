package com.segunfrancis.spacex.ui.home

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.segunfrancis.spacex.ui.home.model.CompanyInfoUi
import com.segunfrancis.spacex.ui.home.model.LaunchesUi
import com.segunfrancis.spacex.util.SpaceXConstants.LAUNCH_STATE_BUNDLE_KEY
import com.segunfrancis.spacex.util.SpaceXConstants.ORDER_BUNDLE_KEY
import com.segunfrancis.spacex.util.SpaceXConstants.QUERY_STRING_FRAGMENT_RESULT_KEY
import com.segunfrancis.spacex.util.SpaceXConstants.YEARS_BUNDLE_KEY
import com.segunfrancis.spacex.util.safeNavigate
import com.segunfrancis.spacex.util.secondOrNull
import com.segunfrancis.spacex.util.showMessage
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.navigation.koinNavGraphViewModel
import com.segunfrancis.spacex.R
import com.segunfrancis.spacex.databinding.FragmentHomeBinding
import java.text.NumberFormat
import java.util.Locale

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding get() = _binding!!
    private val viewModel: HomeViewModel by koinNavGraphViewModel(R.id.navigation_main)
    private val homeAdapter: ConcatAdapter by lazy { ConcatAdapter() }
    private val launchesAdapter: LaunchesAdapter by lazy { LaunchesAdapter() }
    private val companyInfoHeader: HeaderAdapter by lazy { HeaderAdapter(R.string.text_company) }
    private val launchesHeaderAdapter: HeaderAdapter by lazy { HeaderAdapter(R.string.text_launches) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentHomeBinding.bind(view)
        setupObservers()
        setupAdapter()
        binding.toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.filterFragment) {
                viewModel.onMenuItemClicked()
            }
            true
        }

        setFragmentResultListener(QUERY_STRING_FRAGMENT_RESULT_KEY) { _, bundle ->
            val years = bundle[YEARS_BUNDLE_KEY] as String?
            val launchSuccess = bundle[LAUNCH_STATE_BUNDLE_KEY] as String?
            val order = bundle[ORDER_BUNDLE_KEY] as String?
            val launchState = launchSuccess?.equals(getString(R.string.text_successful), true)
            viewModel.setFilters(year = years, launchState = launchState, order = order)
        }
    }

    private fun setupObservers() {
        lifecycleScope.launchWhenStarted {
            viewModel.updatedUiState.collectLatest {
                binding.progressBar.isVisible = it.isLoading
                if (it.launches.isNotEmpty()) {
                    setupLaunchList(it.launches)
                }
                if (it.info != null) {
                    setupCompanyInfo(it.info)
                }
                if (it.errors.isNotEmpty()) {
                    if (it.errors.size >= 2) {
                        // both endpoints failed
                        binding.root.showMessage(message = it.errors.firstOrNull()) {}
                        binding.root.showMessage(message = it.errors.secondOrNull()) {
                            viewModel.resetAllChipIds()
                            viewModel.getHomeScreenDateUpdated()
                        }
                    } else {
                        binding.root.showMessage(message = it.errors.firstOrNull()) {
                            viewModel.resetAllChipIds()
                            viewModel.getHomeScreenDateUpdated()
                        }
                    }
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.interactions.collectLatest {
                when (it) {
                    is HomeViewModel.HomeActions.Navigate -> {
                        findNavController().safeNavigate(it.navDirections)
                    }
                }
            }
        }
    }

    private fun setupAdapter() = with(binding) {
        launchesList.adapter = homeAdapter
        launchesList.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                RecyclerView.VERTICAL
            )
        )
    }

    private fun setupLaunchList(launches: List<LaunchesUi>) = with(binding) {
        launchesAdapter.submitList(launches)
        if (!homeAdapter.adapters.contains(launchesAdapter)) {
            homeAdapter.addAdapter(launchesHeaderAdapter)
            homeAdapter.addAdapter(launchesAdapter)
        }
    }

    private fun setupCompanyInfo(info: CompanyInfoUi) = with(binding) {
        val companyInfo = getString(
            R.string.text_company_info,
            info.companyName,
            info.founderName,
            info.year,
            NumberFormat.getNumberInstance().format(info.employees),
            info.launchSites,
            NumberFormat.getCurrencyInstance(Locale.US).format(info.valuation)
        )

        val companyInfoAdapter = CompanyInfoAdapter(companyInfo)
        if (!homeAdapter.adapters.contains(companyInfoHeader)) {
            if (homeAdapter.adapters.count() >= 2) {
                homeAdapter.addAdapter(0, companyInfoHeader)
                homeAdapter.addAdapter(1, companyInfoAdapter)
            } else {
                homeAdapter.addAdapter(companyInfoHeader)
                homeAdapter.addAdapter(companyInfoAdapter)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}
