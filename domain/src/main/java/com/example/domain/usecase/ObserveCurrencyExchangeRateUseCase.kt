package com.example.domain.usecase

import com.example.domain.model.CurrencyCode
import com.example.domain.repository.AccountRepository
import com.example.utils.LoadState
import com.example.utils.map
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

class ObserveCurrencyExchangeRateUseCase(
    private val accountRepository: AccountRepository,
) {

    suspend operator fun invoke(
        from: CurrencyCode,
        to: CurrencyCode,
        amount: Double,
    ): Flow<LoadState<Double>> =
        accountRepository
            .observeCurrencyExchangeRates()
            .mapLatest { exchangeRates ->
                exchangeRates.map {
                    convert(
                        amount = amount,
                        from = from,
                        to = to,
                        rates = it.rates
                    )
                }
            }
}