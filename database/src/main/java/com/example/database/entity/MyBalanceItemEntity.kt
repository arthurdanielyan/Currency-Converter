package com.example.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

internal const val MY_BALANCES_TABLE_NAME = "my_balances"

@Entity(tableName = MY_BALANCES_TABLE_NAME)
data class MyBalanceItemEntity(
    @PrimaryKey val currencyCode: String,
    val balance: Double,
)