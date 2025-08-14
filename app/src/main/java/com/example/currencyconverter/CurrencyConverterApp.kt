package com.example.currencyconverter

import android.app.Application
import com.example.data.dataModule
import com.example.database.databaseModule
import com.example.domain.domainModule
import com.example.network.httpClientModule
import com.example.presentation.presentationModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module

class CurrencyConverterApp : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(applicationContext)
            androidLogger()
            modules(
                httpClientModule,
                databaseModule,
                dataModule,
                domainModule,
                *presentationModule.toTypedArray(),
                module {
                    single {
                        applicationScope
                    }
                }
            )
        }
    }
}