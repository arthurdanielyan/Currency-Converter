package com.example.domain.exceptions

class InsufficientBalanceException(
    attemptedAmount: Double,
    currentBalance: Double,
    fee: Double,
) : Exception("Insufficient balance. Attempted amount: $attemptedAmount, current balance: $currentBalance, fee: $fee")
