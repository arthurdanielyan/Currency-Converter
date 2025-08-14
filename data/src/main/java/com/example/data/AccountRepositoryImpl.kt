package com.example.data

import com.example.data.mapper.CurrencyExchangeResponseMapper
import com.example.data.mapper.MyBalanceItemEntityMapper
import com.example.data.model.CurrencyExchangeRateResponse
import com.example.database.conversion_info_db.ConversionInformationDataStore
import com.example.database.my_balances_db.dao.MyBalancesDao
import com.example.domain.model.CurrencyCode
import com.example.domain.model.CurrencyExchangeRates
import com.example.domain.model.MyBalanceItem
import com.example.domain.repository.AccountRepository
import com.example.utils.LoadState
import com.example.utils.mapList
import com.example.utils.runSafe
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import kotlin.coroutines.coroutineContext

class AccountRepositoryImpl(
    private val httpClient: HttpClient,
    private val myBalancesDao: MyBalancesDao,
    private val currencyExchangeResponseMapper: CurrencyExchangeResponseMapper,
    private val myBalanceItemEntityMapper: MyBalanceItemEntityMapper,
    private val conversionInformationDataStore: ConversionInformationDataStore,
) : AccountRepository {

    private companion object {
        const val GET_EXCHANGE_RATES = "tasks/api/currency-exchange-rates"
        const val RELOAD_DELAY_MILLIS = 5000L
    }

    private var currencies: List<CurrencyCode>? = null

    override suspend fun observeCurrencyExchangeRates(): Flow<LoadState<CurrencyExchangeRates>> =
        flow {
            emit(LoadState.Loading)
            while (true) {
                try {
                    val response = httpClient
                        .get(GET_EXCHANGE_RATES)
                        .body<CurrencyExchangeRateResponse>()
                    emit(
                        runSafe {
                            currencyExchangeResponseMapper.map(response).also {
                                currencies = it.rates.keys.toList()
                            }
                        }
                    )
                } catch (t: Throwable) {
                    coroutineContext.ensureActive() // propagate up the CancellationException
                    emit(LoadState.Error(t))
                }

                delay(RELOAD_DELAY_MILLIS)
            }
        }

    override suspend fun getCurrencyExchangeRates(): Result<CurrencyExchangeRates> {
        return runCatching {
            val response = httpClient
                .get(GET_EXCHANGE_RATES)
                .body<CurrencyExchangeRateResponse>()

            currencyExchangeResponseMapper.map(response).also {
                currencies = it.rates.keys.toList()
            }
        }
    }

    override suspend fun getCurrencies(): LoadState<List<CurrencyCode>> {
        return runSafe {
            currencies ?: httpClient
                .get(GET_EXCHANGE_RATES)
                .body<CurrencyExchangeRateResponse>()
                .rates.keys.toList()
        }
    }

    override suspend fun observeMyBalances(): Flow<List<MyBalanceItem>> {
        return myBalancesDao.observeMyBalances().mapLatest {
            myBalanceItemEntityMapper.mapList(it)
        }
    }

    override suspend fun getConversionCount(): Int {
        return conversionInformationDataStore.conversionCount.value
    }

    override suspend fun incrementConversionCount() {
        conversionInformationDataStore.incrementConversionCount()
    }

    override suspend fun updateBalances(
        from: CurrencyCode,
        fromAmount: Double,
        to: CurrencyCode,
        toAmount: Double,
    ) {
        myBalancesDao.performExchange(
            from = from,
            fromAmount = fromAmount,
            to = to,
            toAmount = toAmount,
        )
    }
}