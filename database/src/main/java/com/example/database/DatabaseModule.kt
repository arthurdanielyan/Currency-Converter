package com.example.database

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.database.conversion_info_db.ConversionInformationDataStore
import com.example.database.my_balances_db.dao.MyBalancesDao
import com.example.database.my_balances_db.AppDatabase
import com.example.database.my_balances_db.MyBalancesTable
import org.koin.dsl.module

val databaseModule = module {

    single<AppDatabase> {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java, "app.db"
        ).addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                db.execSQL("""
                    INSERT INTO ${MyBalancesTable.TABLE_NAME} 
                    (
                        ${MyBalancesTable.COLUMN_CURRENCY_CODE}, 
                        ${MyBalancesTable.COLUMN_BALANCE}
                    )
                    VALUES ('EUR', 1000.0)
                """.trimIndent())
            }
        }).build()
    }

    single<MyBalancesDao> {
        get<AppDatabase>().myBalancesDao
    }

    single<ConversionInformationDataStore> {
        ConversionInformationDataStore(
            applicationContext = get(),
            applicationScope = get(),
        )
    }
}