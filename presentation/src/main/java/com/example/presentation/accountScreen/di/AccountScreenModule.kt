package com.example.presentation.accountScreen.di

import com.example.presentation.accountScreen.AccountViewModel
import com.example.presentation.accountScreen.mapper.MyBalanceViewStateMapper
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val accountScreenModule = module {
    viewModelOf(::AccountViewModel)

    factoryOf(::MyBalanceViewStateMapper)
}