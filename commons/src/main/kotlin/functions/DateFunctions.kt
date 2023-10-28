package functions

import kotlinx.datetime.LocalDate

fun LocalDate.format() =
    "${dayOfMonth.formatWithZeroPrefix()}.${monthNumber.formatWithZeroPrefix()}.$year"
