package com.example.data.mapper

import com.example.data.model.CurrencyExchangeRateResponse
import com.example.domain.model.CurrencyExchangeRates
import com.example.utils.Mapper

class CurrencyExchangeResponseMapper : Mapper<CurrencyExchangeRateResponse, CurrencyExchangeRates> {

    override fun map(from: CurrencyExchangeRateResponse): CurrencyExchangeRates {
        return CurrencyExchangeRates(
            base = from.baseCurrency,
            rates = from.rates,
        )
    }
}