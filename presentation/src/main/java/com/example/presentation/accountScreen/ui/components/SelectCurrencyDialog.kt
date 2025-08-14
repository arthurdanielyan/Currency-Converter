package com.example.presentation.accountScreen.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.domain.model.CurrencyCode
import com.example.presentation.core.viewState.ComposeList

@Composable
internal fun SelectCurrencyDialog(
    onSelected: (CurrencyCode) -> Unit,
    onDismiss: () -> Unit,
    currencies: ComposeList<CurrencyCode>,
    selectedCurrency: CurrencyCode,
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeightIn(max = 260.dp)
        ) {
            items(
                items = currencies,
                key = { it }
            ) { currencyCode ->
                CurrencyItem(
                    modifier = Modifier.fillMaxWidth(),
                    currencyCode = currencyCode,
                    isSelected = currencyCode == selectedCurrency,
                    onClick = { onSelected(currencyCode) },
                )
            }
        }
    }
}

@Composable
private fun CurrencyItem(
    currencyCode: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(
                if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.inversePrimary
                }
            )
            .clickable(onClick = onClick)
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            )
    ) {
        Text(
            text = currencyCode,
            style = MaterialTheme.typography.bodyLarge,
            color = if (isSelected) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onSurface
            },
        )
    }
}
