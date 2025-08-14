package com.example.domain.model

data class ExchangeCurrenciesResult(
    val fromAmount: Double,
    val fromAmountCurrency: CurrencyCode,
    val toAmount: Double,
    val toAmountCurrency: CurrencyCode,
    val conversionFee: Double,
)