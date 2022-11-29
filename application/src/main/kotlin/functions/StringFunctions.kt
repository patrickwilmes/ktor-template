package functions

fun String.as3CharPrefix() = if (length >= 3) substring(0 until 3).lowercase() else lowercase()

fun String.toRegexForSearch() = Regex("(.)*" + replace(" ", "(.)*").replace(",", "(.)*").lowercase() + "(.)*")
