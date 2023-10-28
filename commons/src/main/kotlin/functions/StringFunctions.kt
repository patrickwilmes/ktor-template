package functions

private const val MAX_CHAR_COUNT = 3

fun String.as3CharPrefix() = if (length >= MAX_CHAR_COUNT)
    substring(0 until MAX_CHAR_COUNT).lowercase()
else lowercase()

fun String.toRegexForSearch() = Regex("(.)*"
    + replace(" ", "(.)*")
        .replace(",", "(.)*").lowercase() + "(.)*")
