package com.example.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrencyExchangeRateResponse(
    @SerialName("base") val baseCurrency: String,
    @SerialName("rates") val rates: Map<String, Double>,
)
