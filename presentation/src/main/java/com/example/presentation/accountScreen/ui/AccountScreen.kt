package com.example.presentation.accountScreen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.presentation.R
import com.example.presentation.accountScreen.AccountUiActions
import com.example.presentation.accountScreen.AccountViewModel
import com.example.presentation.accountScreen.ui.components.ExchangeDialogMessage
import com.example.presentation.accountScreen.ui.components.MyBalanceItem
import com.example.presentation.accountScreen.ui.components.ReceiveCurrencyBlock
import com.example.presentation.accountScreen.ui.components.SelectCurrencyDialog
import com.example.presentation.accountScreen.ui.components.SellCurrencyBlock
import com.example.presentation.accountScreen.viewState.AccountScreenViewState
import com.example.presentation.core.ui.SpacerHeight
import org.koin.androidx.compose.koinViewModel

@Composable
fun AccountScreen() {
    val viewModel = koinViewModel<AccountViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AccountScreen(
        state = uiState,
        onAction = viewModel::onUiAction,
    )

    if (uiState.selectCurrencyDialog.isVisible) {
        SelectCurrencyDialog(
            onSelected = {
                viewModel.onUiAction(
                    AccountUiActions.CurrencySelected(
                        currencyCode = it,
                    )
                )
            },
            onDismiss = {
                viewModel.onUiAction(AccountUiActions.DismissCurrencySelectionDialog)
            },
            currencies = uiState.selectCurrencyDialog.currencies,
            selectedCurrency = uiState.selectCurrencyDialog.selectedCurrency,
        )
    } else if(uiState.conversionDialogMessage != null) {
        ExchangeDialogMessage(
            message = uiState.conversionDialogMessage.orEmpty(),
            onDismiss = {
                viewModel.onUiAction(AccountUiActions.DismissConversionDialog)
            },
        )
    }
}

@Composable
private fun AccountScreen(
    state: AccountScreenViewState,
    onAction: (AccountUiActions) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding(),
    ) {
        TopAppBar()
        SpacerHeight(16.dp)
        Text(
            text = stringResource(R.string.my_balances),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        LazyRow(
            contentPadding = PaddingValues(12.dp),
            horizontalArrangement = Arrangement.spacedBy(32.dp),
        ) {
            items(
                items = state.myBalances,
                key = { it.currencyCode }
            ) {
                MyBalanceItem(
                    myBalance = it,
                )
            }
        }
        SpacerHeight(16.dp)

        Text(
            text = stringResource(R.string.currency_exchange),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        SpacerHeight(12.dp)
        SellCurrencyBlock(
            amount = state.fromCurrencyAmountTextField,
            currencyCode = state.fromCurrencyCode,
            onAmountType = {
                onAction(AccountUiActions.ExchangeAmountType(it))
            },
            onExpandCurrenciesClick = {
                onAction(
                    AccountUiActions.ShowCurrencySelectionDialog(isFromCurrency = true)
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
        HorizontalDivider()
        ReceiveCurrencyBlock(
            buyCurrency = state.targetCurrencyAmount,
            onExpandCurrenciesClick = {
                onAction(
                    AccountUiActions.ShowCurrencySelectionDialog(isFromCurrency = false)
                )
            },
            loadingState = state.loadingState,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.weight(1f))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            onClick = {
                onAction(AccountUiActions.SubmitClick)
            },
        ) {
            if(state.isSubmitButtonLoading) {
                CircularProgressIndicator(
                    color = LocalContentColor.current,
                )
            } else {
                Text(
                    text = stringResource(R.string.submit),
                    style = MaterialTheme.typography.headlineLarge,
                )
            }
        }
        SpacerHeight(16.dp)
    }
}

@Composable
private fun TopAppBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.currency_converter),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )
    }
}
