package com.example.presentation.core.viewState

import com.example.utils.LoadState
import com.example.utils.NoInternetException

enum class DataLoadingState {
    LOADING, SUCCESS, ERROR, NETWORK_ERROR;

    val isError: Boolean
        get() = this == ERROR || this == NETWORK_ERROR
}

fun <T> LoadState<T>.toDataLoadingState(): DataLoadingState {
    return when(this) {
        is LoadState.Error -> {
            if(throwable is NoInternetException) {
                DataLoadingState.NETWORK_ERROR
            } else {
                DataLoadingState.ERROR
            }
        }
        LoadState.Loading -> DataLoadingState.LOADING
        is LoadState.Success<*> -> DataLoadingState.SUCCESS
    }
}
