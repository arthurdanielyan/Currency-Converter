package com.example.database.my_balances_db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.database.my_balances_db.MyBalancesTable

@Entity(tableName = MyBalancesTable.TABLE_NAME)
data class MyBalanceItemEntity(
    @PrimaryKey
    @ColumnInfo(MyBalancesTable.COLUMN_CURRENCY_CODE) val currencyCode: String,
    @ColumnInfo(MyBalancesTable.COLUMN_BALANCE) val balance: Double,
)