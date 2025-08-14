package com.example.presentation

import com.example.presentation.accountScreen.di.accountScreenModule
import com.example.presentation.core.StringProviderImpl
import com.example.utils.StringProvider
import org.koin.dsl.module

val presentationModule = accountScreenModule + module {
    single<StringProvider> {
        StringProviderImpl(
            applicationContext = get(),
        )
    }
}