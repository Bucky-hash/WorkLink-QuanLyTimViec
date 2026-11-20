package com.example.worklink.ui.candidate.cv

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/** Đổi qua lại giữa chữ hiển thị và mili giây UTC mà MaterialDatePicker dùng. */
object CvDates {

    const val FULL_DATE = "dd/MM/yyyy"
    const val MONTH_YEAR = "MM/yyyy"

    private val locale = Locale("vi", "VN")

    private fun formatter(pattern: String) = SimpleDateFormat(pattern, locale).apply {
        timeZone = TimeZone.getTimeZone("UTC")
        isLenient = false
    }

    fun format(utcMillis: Long, pattern: String): String = formatter(pattern).format(Date(utcMillis))

    fun parse(text: String, pattern: String): Long? = try {
        formatter(pattern).parse(text)?.time
    } catch (e: ParseException) {
        null
    }
}
