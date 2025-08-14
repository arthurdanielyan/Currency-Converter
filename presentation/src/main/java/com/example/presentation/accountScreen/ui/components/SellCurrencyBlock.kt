package com.example.presentation.accountScreen.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.presentation.R
import com.example.presentation.accountScreen.viewState.BuyCurrencyViewState
import com.example.presentation.core.ui.SpacerWidth
import com.example.presentation.core.viewState.DataLoadingState

@Composable
internal fun SellCurrencyBlock(
    amount: String,
    currencyCode: String,
    onAmountType: (String) -> Unit,
    onExpandCurrenciesClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier.requiredSize(IconSize),
            imageVector = ImageVector.vectorResource(R.drawable.ic_sell),
            contentDescription = null
        )
        SpacerWidth(8.dp)
        Text(
            text = stringResource(R.string.sell),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        SpacerWidth(8.dp)
        TextField(
            value = amount,
            colors = TextFieldDefaults
                .colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    unfocusedLabelColor = MaterialTheme.colorScheme.tertiaryContainer
                ),
            onValueChange = onAmountType,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
            ),
            modifier = Modifier
                .weight(1f),
            singleLine = true,
        )
        SpacerWidth(4.dp)
        Text(
            text = currencyCode,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
        )
        SpacerWidth(4.dp)
        IconButton(
            onClick = onExpandCurrenciesClick
        ) {
            Icon(
                modifier = Modifier.requiredSize(24.dp),
                imageVector = Icons.Default.KeyboardArrowDown,
                tint = MaterialTheme.colorScheme.onBackground,
                contentDescription = null,
            )
        }
    }
}

@Composable
internal fun ReceiveCurrencyBlock(
    buyCurrency: BuyCurrencyViewState,
    onExpandCurrenciesClick: () -> Unit,
    loadingState: DataLoadingState,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier.requiredSize(IconSize),
            imageVector = ImageVector.vectorResource(R.drawable.ic_receive),
            contentDescription = null
        )
        SpacerWidth(8.dp)
        Text(
            text = stringResource(R.string.receive),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(Modifier.weight(1f, fill = true))

        when (loadingState) {
            DataLoadingState.SUCCESS -> {
                BuyCurrency(buyCurrency = buyCurrency)
            }

            DataLoadingState.LOADING -> {
                if(buyCurrency.amount.isNotEmpty()) {
                    BuyCurrency(buyCurrency = buyCurrency)
                } else {
                    CircularProgressIndicator()
                }
            }

            DataLoadingState.ERROR,
            DataLoadingState.NETWORK_ERROR -> {
                Text(
                    text = stringResource(
                        if (loadingState == DataLoadingState.ERROR) {
                            R.string.error_message
                        } else {
                            R.string.network_error_message
                        }
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }

        SpacerWidth(4.dp)
        if(loadingState.isError.not()) {
            IconButton(
                onClick = onExpandCurrenciesClick
            ) {
                Icon(
                    modifier = Modifier.requiredSize(24.dp),
                    imageVector = Icons.Default.KeyboardArrowDown,
                    tint = MaterialTheme.colorScheme.onBackground,
                    contentDescription = null,
                )
            }
        }
    }
}

@Composable
private fun BuyCurrency(
    buyCurrency: BuyCurrencyViewState
) {
    Text(
        text = "${buyCurrency.amount} ${buyCurrency.currencyCode}",
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.onBackground,
    )
}

private val IconSize = 36.dp
