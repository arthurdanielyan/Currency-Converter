package com.example.utils

interface StringProvider {

    fun string(resId: Int, vararg args: Any): String
}