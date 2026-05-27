package com.kirdevelopment.core.common.date

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

class DateFormatter @Inject constructor() {

    private val inputFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    private val outputFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("ru"))

    fun format(dateString: String): String {
        return try {
            val date = LocalDate.parse(dateString, inputFormatter)
            outputFormatter.format(date)
        } catch (e: Exception) {
            dateString
        }
    }
}
