package com.segunfrancis.spacex.di

import com.segunfrancis.spacex.data.remote.SpaceXClient
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val remoteModule = module {
    single { SpaceXClient.provideApi() }
    single { Dispatchers.IO }
}
