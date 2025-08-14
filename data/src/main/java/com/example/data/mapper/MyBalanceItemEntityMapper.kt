package com.example.data.mapper

import com.example.database.my_balances_db.entity.MyBalanceItemEntity
import com.example.domain.model.MyBalanceItem
import com.example.utils.Mapper

class MyBalanceItemEntityMapper : Mapper<MyBalanceItemEntity, MyBalanceItem> {

    override fun map(from: MyBalanceItemEntity): MyBalanceItem {
        return MyBalanceItem(
            currencyCode = from.currencyCode,
            balance = from.balance,
        )
    }
}