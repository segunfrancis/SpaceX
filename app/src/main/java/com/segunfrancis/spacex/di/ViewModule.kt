package com.segunfrancis.spacex.di

import com.segunfrancis.spacex.ui.home.HomeViewModel
import com.segunfrancis.spacex.ui.view_more.ViewMoreViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModule = module {
    viewModel { HomeViewModel(useCase = get()) }

    viewModel { (articleLink: String, wikiLink: String, videoLink: String) ->
        ViewMoreViewModel(
            articleLink = articleLink,
            wikiLink = wikiLink,
            videoLink = videoLink
        )
    }
}
