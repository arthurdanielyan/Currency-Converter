package com.example.domain.repository

import com.example.domain.model.CurrencyCode
import com.example.domain.model.CurrencyExchangeRates
import com.example.domain.model.MyBalanceItem
import com.example.utils.LoadState
import kotlinx.coroutines.flow.Flow

interface AccountRepository {

    suspend fun observeCurrencyExchangeRates(): Flow<LoadState<CurrencyExchangeRates>>

    suspend fun getCurrencyExchangeRates(): Result<CurrencyExchangeRates>

    suspend fun getCurrencies(): LoadState<List<CurrencyCode>>
    
    suspend fun observeMyBalances(): Flow<List<MyBalanceItem>>

    suspend fun getConversionCount(): Int

    suspend fun incrementConversionCount()

    suspend fun updateBalances(
        from: CurrencyCode,
        fromAmount: Double,
        to: CurrencyCode,
        toAmount: Double,
    )
}