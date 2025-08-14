package com.example.presentation.accountScreen.viewState

import androidx.compose.runtime.Immutable
import com.example.domain.model.CurrencyCode
import com.example.presentation.core.viewState.ComposeList
import com.example.presentation.core.viewState.emptyComposeList

@Immutable
data class SelectCurrencyDialogViewState(
    val isVisible: Boolean = false,
    val isFromCurrency: Boolean = true,
    val currencies: ComposeList<CurrencyCode> = emptyComposeList(),
    val selectedCurrency: CurrencyCode = "",
)