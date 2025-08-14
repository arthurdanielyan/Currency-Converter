package com.example.presentation.core

import android.content.Context
import com.example.utils.StringProvider

internal class StringProviderImpl(
    private val applicationContext: Context,
) : StringProvider {

    override fun string(resId: Int, vararg args: Any): String {
        return applicationContext.getString(resId, *args)
    }
}