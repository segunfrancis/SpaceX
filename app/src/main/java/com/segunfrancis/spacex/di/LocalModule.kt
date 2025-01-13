package com.segunfrancis.spacex.di

import com.segunfrancis.spacex.data.local.SpacexRoomDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val localModule = module {
    single { SpacexRoomDatabase.getDatabase(context = androidContext()) }

    single { get<SpacexRoomDatabase>().getDao() }
}
