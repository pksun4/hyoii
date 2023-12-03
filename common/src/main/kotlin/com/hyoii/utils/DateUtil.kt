package com.hyoii.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DateUtil {

    fun String.toLocalDate(): LocalDate =
        LocalDate.parse(this, DateTimeFormatter.ofPattern("yyyy-MM-dd"))

}
