package com.example.presentation.accountScreen

import com.example.domain.model.CurrencyCode

sealed interface AccountUiActions {

    data class ExchangeAmountType(
        val amount: String,
    ) : AccountUiActions

    data object OnSubmitClick : AccountUiActions

    data class ShowCurrencySelectionDialog(
        val isFromCurrency: Boolean
    ) : AccountUiActions

    data class CurrencySelected(
        val currencyCode: CurrencyCode,
    ) : AccountUiActions

    data object DismissCurrencySelectionDialog : AccountUiActions

    data object DismissConversionDialog : AccountUiActions

    data object SubmitClick : AccountUiActions
}