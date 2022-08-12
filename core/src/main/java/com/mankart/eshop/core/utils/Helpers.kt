package com.mankart.eshop.core.utils

import android.icu.text.NumberFormat
import java.util.*

object Helpers {
    fun Int.formatIDR(): String {
        val localeID = Locale("in", "ID")
        val formatter = NumberFormat.getCurrencyInstance(localeID)
        return formatter.format(this)
    }
}