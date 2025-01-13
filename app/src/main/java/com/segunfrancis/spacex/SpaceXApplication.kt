package com.segunfrancis.spacex

import android.app.Application
import com.segunfrancis.spacex.di.localModule
import com.segunfrancis.spacex.di.remoteModule
import com.segunfrancis.spacex.di.repositoryModule
import com.segunfrancis.spacex.di.useCaseModule
import com.segunfrancis.spacex.di.viewModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class SpaceXApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidContext(this@SpaceXApplication)
            modules(remoteModule, localModule, repositoryModule, useCaseModule, viewModule)
        }
    }
}
