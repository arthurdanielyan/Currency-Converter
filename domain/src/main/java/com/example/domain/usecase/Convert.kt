package com.example.domain.usecase

import com.example.domain.model.CurrencyCode

internal fun convert(
    amount: Double,
    from: CurrencyCode,
    to: CurrencyCode,
    rates: Map<CurrencyCode, Double>
): Double {
    val rateFrom = rates[from] ?: throw IllegalArgumentException("Unknown currency: $from")
    val rateTo = rates[to] ?: throw IllegalArgumentException("Unknown currency: $to")
    return amount * (rateTo / rateFrom)
}