package com.example.presentation.accountScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.exceptions.InsufficientBalanceException
import com.example.domain.model.CurrencyCode
import com.example.domain.usecase.ExchangeCurrenciesUseCase
import com.example.domain.usecase.GetAvailableCurrenciesUseCase
import com.example.domain.usecase.GetMyCurrenciesUseCase
import com.example.domain.usecase.ObserveCurrencyExchangeRateUseCase
import com.example.domain.usecase.ObserveMyBalancesUseCase
import com.example.presentation.R
import com.example.presentation.accountScreen.mapper.MyBalanceViewStateMapper
import com.example.presentation.accountScreen.mapper.formatAmount
import com.example.presentation.accountScreen.viewState.AccountScreenViewState
import com.example.presentation.accountScreen.viewState.BuyCurrencyViewState
import com.example.presentation.accountScreen.viewState.MyBalanceViewState
import com.example.presentation.accountScreen.viewState.SelectCurrencyDialogViewState
import com.example.presentation.core.combine
import com.example.presentation.core.stateInWhileSubscribed
import com.example.presentation.core.viewState.DataLoadingState
import com.example.presentation.core.viewState.toComposeList
import com.example.presentation.core.viewState.toDataLoadingState
import com.example.utils.NoInternetException
import com.example.utils.StringProvider
import com.example.utils.dataOrNull
import com.example.utils.mapList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class AccountViewModel(
    private val observeCurrencyExchangeRateUseCase: ObserveCurrencyExchangeRateUseCase,
    private val observeMyBalancesUseCase: ObserveMyBalancesUseCase,
    private val exchangeCurrenciesUseCase: ExchangeCurrenciesUseCase,
    private val myBalanceViewStateMapper: MyBalanceViewStateMapper,
    private val getAvailableCurrenciesUseCase: GetAvailableCurrenciesUseCase,
    private val getMyCurrenciesUseCase: GetMyCurrenciesUseCase,
    private val stringProvider: StringProvider,
) : ViewModel() {

    private val myBalances = MutableStateFlow(emptyList<MyBalanceViewState>())
    private val fromCurrencyAmountTextField = MutableStateFlow("1")
    private val fromCurrencyCode = MutableStateFlow("EUR")
    private val targetCurrencyAmount = MutableStateFlow(BuyCurrencyViewState())
    private val selectCurrencyDialog = MutableStateFlow(SelectCurrencyDialogViewState())
    private val conversionDialogMessage = MutableStateFlow<String?>(null)
    private val isSubmitButtonLoading = MutableStateFlow(false)
    private val loadingState = MutableStateFlow(DataLoadingState.LOADING)

    val uiState = combine(
        myBalances,
        fromCurrencyAmountTextField,
        fromCurrencyCode,
        targetCurrencyAmount,
        selectCurrencyDialog,
        conversionDialogMessage,
        isSubmitButtonLoading,
        loadingState,
    ) { myBalances,
        sellCurrencyAmountTextField,
        sellCurrencyCode,
        buyCurrencyAmount,
        selectCurrencyDialog,
        conversionDialogMessage,
        isSubmitButtonLoading,
        loadingState ->

        AccountScreenViewState(
            myBalances = myBalances.toComposeList(),
            fromCurrencyAmountTextField = sellCurrencyAmountTextField,
            fromCurrencyCode = sellCurrencyCode,
            targetCurrencyAmount = buyCurrencyAmount,
            selectCurrencyDialog = selectCurrencyDialog,
            conversionDialogMessage = conversionDialogMessage,
            isSubmitButtonLoading = isSubmitButtonLoading,
            loadingState = loadingState,
        )
    }.stateInWhileSubscribed(viewModelScope, AccountScreenViewState())

    init {
        observeCurrencyExchangeRate()
        observeMyBalances()
    }

    fun onUiAction(action: AccountUiActions) {
        when (action) {
            is AccountUiActions.ExchangeAmountType -> {
                fromCurrencyAmountTextField.update { action.amount }
            }

            AccountUiActions.OnSubmitClick -> Unit

            is AccountUiActions.ShowCurrencySelectionDialog -> {
                showSelectCurrencyDialog(isFromCurrency = action.isFromCurrency)
            }

            is AccountUiActions.CurrencySelected -> {
                selectCurrency(
                    currencyCode = action.currencyCode,
                )
            }

            AccountUiActions.DismissCurrencySelectionDialog -> {
                selectCurrencyDialog.update { it.copy(isVisible = false) }
            }

            AccountUiActions.SubmitClick -> {
                if (isSubmitButtonLoading.value.not()) {
                    exchangeCurrencies()
                }
            }

            AccountUiActions.DismissConversionDialog -> {
                conversionDialogMessage.update { null }
            }
        }
    }

    private fun exchangeCurrencies() {
        isSubmitButtonLoading.update { true }
        viewModelScope.launch {
            exchangeCurrenciesUseCase(
                from = fromCurrencyCode.value,
                to = targetCurrencyAmount.value.currencyCode,
                amount = fromCurrencyAmountTextField.value.toDouble(),
            ).also { loadState ->
                isSubmitButtonLoading.update { false }
                loadState
                    .onSuccess { data ->
                        buildString {
                            append(
                                stringProvider.string(
                                    R.string.conversion_message,
                                    formatAmount(data.fromAmount),
                                    data.fromAmountCurrency,
                                    formatAmount(data.toAmount),
                                    data.toAmountCurrency,
                                )
                            )
                            if (data.conversionFee > 0) {
                                append(" ")
                                append(
                                    stringProvider.string(
                                        R.string.conversion_message_fee,
                                        formatAmount(data.conversionFee),
                                        data.fromAmountCurrency,
                                    )
                                )
                            }
                        }.also { conversionMessage ->
                            conversionDialogMessage.update { conversionMessage }
                        }
                    }.onFailure { throwable ->
                        conversionDialogMessage.update {
                            when(throwable) {
                                is InsufficientBalanceException ->
                                    stringProvider.string(R.string.insufficient_balance)
                                is NoInternetException ->
                                    stringProvider.string(R.string.network_error_message)
                                else ->
                                    stringProvider.string(R.string.error_message)
                            }
                        }
                    }
            }
        }
    }

    private fun selectCurrency(
        currencyCode: CurrencyCode,
    ) {
        if (selectCurrencyDialog.value.isFromCurrency) {
            fromCurrencyCode.update { currencyCode }
        } else {
            targetCurrencyAmount.update {
                it.copy(currencyCode = currencyCode)
            }
        }
        selectCurrencyDialog.update { it.copy(isVisible = false) }
    }

    private fun showSelectCurrencyDialog(
        isFromCurrency: Boolean,
    ) {
        viewModelScope.launch {
            val currencies = if (isFromCurrency) {
                getMyCurrenciesUseCase()
            } else {
                getAvailableCurrenciesUseCase().dataOrNull() ?: return@launch
            }
            selectCurrencyDialog.update { state ->
                state.copy(
                    isVisible = true,
                    isFromCurrency = isFromCurrency,
                    currencies = currencies
                        .filter {
                            if (isFromCurrency) {
                                it != targetCurrencyAmount.value.currencyCode
                            } else {
                                it != fromCurrencyCode.value
                            }
                        }
                        .toComposeList(),
                    selectedCurrency = if (isFromCurrency) {
                        fromCurrencyCode.value
                    } else {
                        targetCurrencyAmount.value.currencyCode
                    },
                )
            }
        }
    }

    private fun observeCurrencyExchangeRate() {
        viewModelScope.launch {
            combine(
                fromCurrencyCode,
                targetCurrencyAmount,
                fromCurrencyAmountTextField
                    .mapLatest { it.toDoubleOrNull() ?: 0.0 },
                ::Triple
            ).flatMapLatest { (fromCurrency, toCurrency, amount) ->
                observeCurrencyExchangeRateUseCase(
                    from = fromCurrency,
                    to = toCurrency.currencyCode,
                    amount = amount,
                )
            }.collectLatest { resultAmount ->
                resultAmount.dataOrNull()?.let { amount ->
                    targetCurrencyAmount.update {
                        it.copy(
                            amount = formatAmount(amount),
                        )
                    }
                }
                loadingState.update {
                    resultAmount.toDataLoadingState()
                }
            }
        }
    }

    private fun observeMyBalances() {
        viewModelScope.launch {
            observeMyBalancesUseCase()
                .mapLatest { myBalanceViewStateMapper.mapList(it) }
                .collect(myBalances)
        }
    }
}