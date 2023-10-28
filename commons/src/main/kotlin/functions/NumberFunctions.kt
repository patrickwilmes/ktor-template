package functions

fun UShort.formatWithZeroPrefix(totalLength: UShort = 2U): String =
    toString().padStart(totalLength.toInt(), '0')

fun UInt.formatWithZeroPrefix(totalLength: UShort = 2U): String =
    toString().padStart(totalLength.toInt(), '0')

fun Int.formatWithZeroPrefix(totalLength: UShort = 2U): String =
    toString().padStart(totalLength.toInt(), '0')
