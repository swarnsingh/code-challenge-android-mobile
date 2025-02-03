package com.swarn.terminalapp.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

const val TIMESTAMP_FORMAT = "dd MMM yyyy, hh:mm a"

fun convertTimestampToFormattedDateTime(timestamp: Long): String {
    val date = Date(timestamp)
    val calendar = Calendar.getInstance()
    calendar.time = date

    val formatter = SimpleDateFormat(TIMESTAMP_FORMAT, Locale.getDefault())
    return formatter.format(calendar.time)
}

fun formatAmount(value: Double): Double {
    return String.format(Locale.getDefault(), "%.2f", value).toDouble()
}