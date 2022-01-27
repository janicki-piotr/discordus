package pl.redny.common.date

import java.time.LocalDateTime

interface DateTime {
    companion object {
        const val FORMATTER_DD_MM_YYYY = "dd/MM/yyyy"
        const val FORMATTER_DD_MM_YYYY_DASHES = "dd-MM-yyyy"
        const val FORMATTER_DD_MM_YYYY_HH_MM_SS = "yyyy-MM-dd HH:mm:ss"
        const val FORMATTER_HH_MM = "HH:mm"
        const val FORMATTER_HH_MM_SS = "HH:mm:ss"
    }

    fun now(): LocalDateTime

    fun now(dateTimeFormat : String): String
}