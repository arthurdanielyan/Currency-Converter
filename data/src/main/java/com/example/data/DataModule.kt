package com.example.data

import com.example.data.mapper.CurrencyExchangeResponseMapper
import com.example.data.mapper.MyBalanceItemEntityMapper
import com.example.domain.repository.AccountRepository
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataModule = module {
    single<AccountRepository> {
        AccountRepositoryImpl(
            httpClient = get(),
            myBalancesDao = get(),
            currencyExchangeResponseMapper = get(),
            myBalanceItemEntityMapper = get(),
            conversionInformationDataStore = get(),
        )
    }

    factoryOf(::CurrencyExchangeResponseMapper)
    factoryOf(::MyBalanceItemEntityMapper)
}