package com.example.presentation.accountScreen

import androidx.compose.runtime.Immutable
import com.example.domain.model.CurrencyCode
import com.example.presentation.core.ComposeList

@Immutable
data class AccountViewState(
    val myBalances: ComposeList<MyBalanceViewState>,
    val sellCurrencyAmountTextField: String,
    val sellCurrencyCode: CurrencyCode,
    val buyCurrencyAmount: String,
    val buyCurrencyCode: CurrencyCode,
)

@Immutable
data class MyBalanceViewState(
    val formattedBalance: String,
    val balance: Double,
    val currencyCode: CurrencyCode,
)
