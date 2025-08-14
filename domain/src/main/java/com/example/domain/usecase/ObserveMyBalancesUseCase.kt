package com.example.domain.usecase

import com.example.domain.model.MyBalanceItem
import com.example.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow

class ObserveMyBalancesUseCase(
    private val accountRepository: AccountRepository,
) {

    suspend operator fun invoke(): Flow<List<MyBalanceItem>> {
        return accountRepository.observeMyBalances()
    }
}