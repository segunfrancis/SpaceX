package com.segunfrancis.spacex.ui.view_more

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.segunfrancis.spacex.ui.view_more.ViewMoreViewModel.*
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import com.segunfrancis.spacex.databinding.FragmentViewMoreBinding

class ViewMoreFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentViewMoreBinding? = null
    private val binding: FragmentViewMoreBinding get() = _binding!!
    private val args by navArgs<ViewMoreFragmentArgs>()
    private val viewModel by viewModel<ViewMoreViewModel> {
        parametersOf(
            args.articleLink,
            args.wikiLink,
            args.videoLink
        )
    }
    private val viewMoreAdapter by lazy { ViewMoreAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewMoreBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
    }

    private fun setupObservers() {
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collectLatest {
                when (it) {
                    is ViewMoreUiState.Content -> setupViewMoreList(it.data)
                    ViewMoreUiState.NoContent -> setupEmptyContent()
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.interaction.collectLatest {
                when (it) {
                    is ViewMoreAction.LaunchIntent -> openLink(it.link)
                }
            }
        }
    }

    private fun setupViewMoreList(items: List<ViewMoreItem>) = with(binding) {
        viewMoreList.apply {
            adapter = viewMoreAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
        }
        viewMoreAdapter.submitList(items)
    }

    private fun setupEmptyContent() {
        binding.textNoContent.isVisible = true
    }

    private fun openLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}
