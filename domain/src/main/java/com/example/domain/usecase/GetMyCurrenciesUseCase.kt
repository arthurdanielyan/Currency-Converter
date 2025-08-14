package com.example.domain.usecase

import com.example.domain.model.CurrencyCode
import com.example.domain.repository.AccountRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest

class GetMyCurrenciesUseCase(
    private val accountRepository: AccountRepository,
) {

    suspend operator fun invoke(): List<CurrencyCode> {
        return accountRepository
            .observeMyBalances()
            .mapLatest {
                it.map { it.currencyCode }
            }.first()
    }
}