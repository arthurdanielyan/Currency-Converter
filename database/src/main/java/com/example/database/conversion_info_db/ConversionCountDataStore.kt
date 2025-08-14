package com.example.database.conversion_info_db

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ConversionInformationDataStore(
    private val applicationContext: Context,
    applicationScope: CoroutineScope,
) {

    private val Context.conversionInformationStore by preferencesDataStore(
        name = DATA_STORE_NAME,
        scope = applicationScope,
    )
    private val dataStore
        get() = applicationContext.conversionInformationStore

    val conversionCount: StateFlow<Int> = dataStore.data.map { preferences ->
        preferences[conversionCountKey] ?: 0
    }.stateIn(applicationScope, SharingStarted.Eagerly, 0)

    suspend fun incrementConversionCount() {
        dataStore.edit {
            it[conversionCountKey] = (it[conversionCountKey] ?: 0) + 1
        }
    }

    private companion object {
        const val DATA_STORE_NAME = "conversion_information"

        val conversionCountKey = intPreferencesKey("conversion_count")
    }
}