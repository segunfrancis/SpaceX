package com.segunfrancis.spacex.di

import com.segunfrancis.spacex.usecase.HomeUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { HomeUseCase(repository = get(), dispatcher = get()) }
}
