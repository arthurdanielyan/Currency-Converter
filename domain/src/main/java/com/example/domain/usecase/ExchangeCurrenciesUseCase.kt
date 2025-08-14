package com.example.domain.usecase

import com.example.domain.exceptions.InsufficientBalanceException
import com.example.domain.model.CurrencyCode
import com.example.domain.model.ExchangeCurrenciesResult
import com.example.domain.repository.AccountRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first

class ExchangeCurrenciesUseCase(
    private val accountRepository: AccountRepository,
) {

    private companion object {
        private const val MIN_FEE_FREE_CONVERSIONS = 5
        private const val FEE_PERCENTAGE = 0.07f
    }

    suspend operator fun invoke(
        from: CurrencyCode,
        to: CurrencyCode,
        amount: Double,
    ): Result<ExchangeCurrenciesResult> {
        return coroutineScope {
            val balancesDeferred = async {
                accountRepository
                    .observeMyBalances()
                    .first()
            }

            val exchangeRatesDeferred = async {
                accountRepository.getCurrencyExchangeRates()
            }

            val balances = balancesDeferred.await()
            val exchangeRates = exchangeRatesDeferred.await()
                .map {
                    it.rates
                }.getOrElse {
                    return@coroutineScope Result.failure(it)
                }

            val balanceFrom = balances.firstOrNull { it.currencyCode == from }?.balance
                ?: throw IllegalArgumentException("Balance $from not found")
            val balanceTo = balances.firstOrNull { it.currencyCode == to }?.balance
                ?: 0.0

            val fee = if(accountRepository.getConversionCount() > MIN_FEE_FREE_CONVERSIONS) {
                amount * FEE_PERCENTAGE
            } else {
                0.0
            }

            if (balanceFrom < amount + fee) {
                return@coroutineScope Result.failure(
                    exception = InsufficientBalanceException(
                        attemptedAmount = amount,
                        currentBalance = balanceFrom,
                        fee = fee,
                    )
                )
            } else {
                val toAmount = convert(
                    amount = amount,
                    from = from,
                    to = to,
                    rates = exchangeRates,
                )
                accountRepository.updateBalances(
                    from = from,
                    fromAmount = balanceFrom - amount - fee,
                    to = to,
                    toAmount = balanceTo + toAmount,
                )
                accountRepository.incrementConversionCount()
                return@coroutineScope Result.success(
                    ExchangeCurrenciesResult(
                        fromAmount = amount,
                        fromAmountCurrency = from,
                        toAmount = toAmount,
                        toAmountCurrency = to,
                        conversionFee = fee,
                    )
                )
            }
        }
    }
}