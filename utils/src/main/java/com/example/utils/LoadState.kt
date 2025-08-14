package com.example.utils

sealed interface LoadState<out T> {
    data object Loading : LoadState<Nothing>

    data class Success<T>(val data: T) : LoadState<T>

    data class Error (
        val throwable: Throwable
    ) : LoadState<Nothing>

}

fun <T> LoadState<T>.dataOrNull(): T? =
    when (this) {
        is LoadState.Success -> data
        else -> null
    }

inline fun <T> runSafe(block: () -> T): LoadState<T> {
    return try {
        LoadState.Success(block())
    } catch (t: Throwable) {
        LoadState.Error(throwable = t)
    }
}

inline fun <T, R> LoadState<T>.map(transform: (T) -> R): LoadState<R> {
    return when (this) {
        is LoadState.Error -> LoadState.Error(throwable)
        is LoadState.Loading -> LoadState.Loading
        is LoadState.Success -> {
            runSafe {
                transform(this.data)
            }
        }
    }
}
