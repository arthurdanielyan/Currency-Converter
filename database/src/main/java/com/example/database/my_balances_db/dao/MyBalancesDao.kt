package com.example.database.my_balances_db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.database.my_balances_db.MyBalancesTable
import com.example.database.my_balances_db.entity.MyBalanceItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class MyBalancesDao {

    @Upsert
    abstract suspend fun upsertExchangeRate(rate: MyBalanceItemEntity)

    @Query("SELECT * FROM ${MyBalancesTable.TABLE_NAME}")
    abstract fun observeMyBalances(): Flow<List<MyBalanceItemEntity>>

    @Transaction
    open suspend fun performExchange(
        from: String,
        fromAmount: Double,
        to: String,
        toAmount: Double
    ) {
        upsertExchangeRate(MyBalanceItemEntity(from, fromAmount))
        upsertExchangeRate(MyBalanceItemEntity(to, toAmount))
    }
}