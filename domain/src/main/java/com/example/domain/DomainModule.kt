package com.example.domain

import com.example.domain.usecase.ExchangeCurrenciesUseCase
import com.example.domain.usecase.GetAvailableCurrenciesUseCase
import com.example.domain.usecase.GetMyCurrenciesUseCase
import com.example.domain.usecase.ObserveCurrencyExchangeRateUseCase
import com.example.domain.usecase.ObserveMyBalancesUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {
    factoryOf(::ObserveCurrencyExchangeRateUseCase)
    factoryOf(::ObserveMyBalancesUseCase)
    factoryOf(::ExchangeCurrenciesUseCase)
    factoryOf(::GetAvailableCurrenciesUseCase)
    factoryOf(::GetMyCurrenciesUseCase)
}