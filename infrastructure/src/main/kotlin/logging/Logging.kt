package logging

import org.slf4j.LoggerFactory

fun dbgln(message: String) {
    object {}.dbgln(message)
}

fun dbgln(message: String, throwable: Throwable) {
    println("$message, caused by ${throwable.message}")
    throwable.printStackTrace()
}

inline fun <reified T> T.dbgln(message: String, loggerName: String = T::class.simpleName ?: "Heimdall"): T {
    LoggerFactory.getLogger(loggerName).debug(message)
    return this
}
