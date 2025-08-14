package com.example.presentation.accountScreen.mapper

import java.text.DecimalFormat

internal fun formatAmount(amount: Double): String {
    val df = DecimalFormat(FORMAT_PATTERN)
    return df.format(amount)
}

private const val FORMAT_PATTERN = "#.##"
