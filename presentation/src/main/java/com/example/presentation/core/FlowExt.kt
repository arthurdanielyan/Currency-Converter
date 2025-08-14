package com.example.presentation.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

fun <T> Flow<T>.stateInWhileSubscribed(
    scope: CoroutineScope,
    initialValue: T,
) = stateIn(
    scope = scope,
    started = SharingStarted.WhileSubscribed(DEFAULT_STOP_TIMEOUT),
    initialValue = initialValue,
)

@Suppress("UNCHECKED_CAST")
fun <T1, T2, T3, T4, T5, T6, T7, T8, R> combine(
    flow1: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    flow7: Flow<T7>,
    flow8: Flow<T8>,
    transform: suspend (T1, T2, T3, T4, T5, T6, T7, T8) -> R
) = kotlinx.coroutines.flow.combine(
    flow1,
    flow2,
    flow3,
    flow4,
    flow5,
    flow6,
    flow7,
    flow8,
) { flows ->
    transform(
        flows[0] as T1,
        flows[1] as T2,
        flows[2] as T3,
        flows[3] as T4,
        flows[4] as T5,
        flows[5] as T6,
        flows[6] as T7,
        flows[7] as T8,
    )
}

private const val DEFAULT_STOP_TIMEOUT = 5000L