package com.segunfrancis.spacex.ui.view_more

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.segunfrancis.spacex.R

class ViewMoreViewModel(
    private val articleLink: String?,
    private val wikiLink: String?,
    private val videoLink: String?
) : ViewModel() {

    private val _uiState = MutableStateFlow<ViewMoreUiState>(ViewMoreUiState.NoContent)
    val uiState get() = _uiState.asStateFlow()

    private val _interaction = MutableSharedFlow<ViewMoreAction>()
    val interaction get() = _interaction.asSharedFlow()

    init {
        _uiState.update { if (getContent().isNotEmpty()) ViewMoreUiState.Content(data = getContent()) else ViewMoreUiState.NoContent }
    }

    private fun getContent(): List<ViewMoreItem> {
        val content = mutableListOf<ViewMoreItem>()
        articleLink?.let {
            content.add(
                ViewMoreItem(
                    title = R.string.text_article,
                    icon = R.drawable.article,
                    onclick = {
                        viewModelScope.launch {
                            _interaction.emit(ViewMoreAction.LaunchIntent(link = it))
                        }
                    }
                )
            )
        }
        wikiLink?.let {
            content.add(
                ViewMoreItem(
                    title = R.string.text_wikipedia,
                    icon = R.drawable.wikipedia,
                    onclick = {
                        viewModelScope.launch {
                            _interaction.emit(ViewMoreAction.LaunchIntent(link = it))
                        }
                    }
                )
            )
        }
        videoLink?.let {
            content.add(
                ViewMoreItem(
                    title = R.string.text_video,
                    icon = R.drawable.youtube,
                    onclick = {
                        viewModelScope.launch {
                            _interaction.emit(ViewMoreAction.LaunchIntent(link = it))
                        }
                    }
                )
            )
        }
        return content
    }

    sealed class ViewMoreUiState {
        object NoContent : ViewMoreUiState()
        data class Content(val data: List<ViewMoreItem>) : ViewMoreUiState()
    }

    sealed class ViewMoreAction {
        data class LaunchIntent(val link: String) : ViewMoreAction()
    }
}
