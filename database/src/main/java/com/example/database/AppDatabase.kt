package com.example.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.database.dao.MyBalancesDao
import com.example.database.entity.MyBalanceItemEntity

@Database(
    entities = [MyBalanceItemEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract val myBalancesDao: MyBalancesDao
}