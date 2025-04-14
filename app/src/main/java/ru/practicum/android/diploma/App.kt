package ru.practicum.android.diploma

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import ru.practicum.android.diploma.di.dataModule

const val APP_PREFERENCES = "practicum_diploma_preferences"

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule)
        }
    }
}
