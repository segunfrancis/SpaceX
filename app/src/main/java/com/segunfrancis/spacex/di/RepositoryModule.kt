package com.segunfrancis.spacex.di

import com.segunfrancis.spacex.data.repository.SpaceXRepository
import com.segunfrancis.spacex.data.repository.SpaceXRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<SpaceXRepository> { SpaceXRepositoryImpl(dao = get(), api = get(), dispatcher = get()) }
}
