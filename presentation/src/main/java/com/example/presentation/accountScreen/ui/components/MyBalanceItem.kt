package com.example.presentation.accountScreen.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.presentation.accountScreen.viewState.MyBalanceViewState

@Composable
internal fun MyBalanceItem(
    myBalance: MyBalanceViewState
) {
    Text(
        text = "${myBalance.formattedBalance} ${myBalance.currencyCode}",
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onBackground,
    )
}