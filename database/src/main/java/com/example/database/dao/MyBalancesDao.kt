package com.example.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.database.entity.MY_BALANCES_TABLE_NAME
import com.example.database.entity.MyBalanceItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MyBalancesDao {

    @Upsert
    suspend fun upsertExchangeRate(rate: MyBalanceItemEntity)

    @Query("SELECT * FROM $MY_BALANCES_TABLE_NAME")
    fun observeMyBalances(): Flow<List<MyBalanceItemEntity>>
}