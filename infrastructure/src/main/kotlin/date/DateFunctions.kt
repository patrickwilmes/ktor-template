package date

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun now() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
fun nowTime() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
