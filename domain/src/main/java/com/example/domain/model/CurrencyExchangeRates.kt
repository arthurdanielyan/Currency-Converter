package com.example.domain.model

data class CurrencyExchangeRates(
    val base: CurrencyCode,
    val rates: Map<CurrencyCode, Double>,
)
