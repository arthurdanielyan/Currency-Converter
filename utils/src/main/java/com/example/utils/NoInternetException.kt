package com.example.utils

class NoInternetException(cause: Throwable? = null) :
    Exception("No internet connection", cause)
