package com.example.presentation.accountScreen.mapper

import com.example.domain.model.MyBalanceItem
import com.example.presentation.accountScreen.viewState.MyBalanceViewState
import com.example.utils.Mapper

class MyBalanceViewStateMapper : Mapper<MyBalanceItem, MyBalanceViewState> {

    override fun map(from: MyBalanceItem): MyBalanceViewState {
        return MyBalanceViewState(
            formattedBalance = formatAmount(from.balance),
            balance = from.balance,
            currencyCode = from.currencyCode,
        )
    }
}