package com.example.presentation.accountScreen.viewState

import androidx.compose.runtime.Immutable
import com.example.domain.model.CurrencyCode
import com.example.presentation.core.viewState.ComposeList
import com.example.presentation.core.viewState.DataLoadingState
import com.example.presentation.core.viewState.emptyComposeList

@Immutable
data class AccountScreenViewState(
    val myBalances: ComposeList<MyBalanceViewState> = emptyComposeList(),
    val fromCurrencyAmountTextField: String = "",
    val fromCurrencyCode: CurrencyCode = "",
    val targetCurrencyAmount: BuyCurrencyViewState = BuyCurrencyViewState(),
    val selectCurrencyDialog: SelectCurrencyDialogViewState = SelectCurrencyDialogViewState(),
    val conversionDialogMessage: String? = null,
    val isSubmitButtonLoading: Boolean = false,
    val loadingState: DataLoadingState = DataLoadingState.LOADING,
)

@Immutable
data class BuyCurrencyViewState(
    val amount: String = "",
    val currencyCode: CurrencyCode = "USD",
)

@Immutable
data class MyBalanceViewState(
    val formattedBalance: String,
    val balance: Double,
    val currencyCode: CurrencyCode,
)
