package com.example.domain.usecase

import com.example.domain.model.CurrencyCode
import com.example.domain.repository.AccountRepository
import com.example.utils.LoadState

class GetAvailableCurrenciesUseCase(
    private val accountRepository: AccountRepository,
) {

    suspend operator fun invoke(): LoadState<List<CurrencyCode>> {
        return accountRepository
            .getCurrencies()
    }
}