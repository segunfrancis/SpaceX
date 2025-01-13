package com.segunfrancis.spacex.ui.filter

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.segunfrancis.spacex.ui.home.HomeViewModel
import com.segunfrancis.spacex.util.SpaceXConstants.LAUNCH_STATE_BUNDLE_KEY
import com.segunfrancis.spacex.util.SpaceXConstants.ORDER_BUNDLE_KEY
import com.segunfrancis.spacex.util.SpaceXConstants.QUERY_STRING_FRAGMENT_RESULT_KEY
import com.segunfrancis.spacex.util.SpaceXConstants.YEARS_BUNDLE_KEY
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.navigation.koinNavGraphViewModel
import com.segunfrancis.spacex.R
import com.segunfrancis.spacex.databinding.FragmentFilterBinding

class FilterFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentFilterBinding? = null
    private val binding: FragmentFilterBinding get() = _binding!!
    private val args by navArgs<FilterFragmentArgs>()
    private val viewModel by koinNavGraphViewModel<HomeViewModel>(R.id.navigation_main)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupYearsChip()
        setupObservers()
        setupClickListeners()
    }

    private fun setupYearsChip() {
        args.years.forEachIndexed { index, s ->
            val chip = Chip(requireContext(), null, R.attr.customChipStyle)
            chip.text = s
            chip.id = index
            binding.yearsFilterChipGroup.addView(chip, index)
        }
    }

    private fun setupObservers() = with(binding) {
        lifecycleScope.launchWhenStarted {
            viewModel.chipState.collectLatest {
                yearsFilterChipGroup.check(it.yearsId)
                launchStateChipGroup.check(it.launchStateId)
                orderByChipGroup.check(it.orderId)
            }
        }
    }

    private fun setupClickListeners() = with(binding) {
        buttonClearFilters.setOnClickListener {
            yearsFilterChipGroup.clearCheck()
            launchStateChipGroup.clearCheck()
            orderByChipGroup.clearCheck()
        }
    }

    private fun getChipText(selectedChipId: Int): String {
        return binding.root.findViewById<Chip>(selectedChipId).text.toString()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        binding.apply {
            setFragmentResult(
                QUERY_STRING_FRAGMENT_RESULT_KEY, bundleOf(
                    YEARS_BUNDLE_KEY to if (yearsFilterChipGroup.checkedChipId == -1) null else getChipText(
                        yearsFilterChipGroup.checkedChipId
                    ),
                    LAUNCH_STATE_BUNDLE_KEY to if (launchStateChipGroup.checkedChipId == -1) null else getChipText(
                        launchStateChipGroup.checkedChipId
                    ),
                    ORDER_BUNDLE_KEY to if (orderByChipGroup.checkedChipId == -1) null else getChipText(
                        orderByChipGroup.checkedChipId
                    )
                )
            )
        }
    }

    override fun onStop() {
        super.onStop()
        binding.apply {
            viewModel.updateYearsId(yearsFilterChipGroup.checkedChipId)
            viewModel.updateLaunchStateId(launchStateChipGroup.checkedChipId)
            viewModel.updateOrderId(orderByChipGroup.checkedChipId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}
