package com.elfiky.ibtikarandroidtask.app

import android.app.Application
import com.elfiky.data.di.dataModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MoviesApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MoviesApp)
            modules(dataModule)
        }
    }

}