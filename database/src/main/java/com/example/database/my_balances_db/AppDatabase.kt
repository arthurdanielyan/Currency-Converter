package com.example.database.my_balances_db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.database.my_balances_db.dao.MyBalancesDao
import com.example.database.my_balances_db.entity.MyBalanceItemEntity

@Database(
    entities = [MyBalanceItemEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract val myBalancesDao: MyBalancesDao
}