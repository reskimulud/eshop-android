package com.mankart.eshop.core.utils


import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

object Helpers {

    private fun simpleDateFormat(format: String) = SimpleDateFormat(format, Locale.getDefault())

    fun Int.formatIDR(): String {
        val localeID = Locale("in", "ID")
        val formatter = NumberFormat.getCurrencyInstance(localeID)
        return formatter.format(this)
    }

    fun getCurrentDate(): String {
        val dateFormat = simpleDateFormat("yyyy/MM/dd HH:mm:ss")
        val date = Date()
        return dateFormat.format(date)
    }

    fun Long.timestampToDate() : String {
        val simpleDateFormat = simpleDateFormat("dd MMMM yyyy, HH:mm:ss")
        return simpleDateFormat.format(this)
    }
}