package common

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.util.pipeline.*
import logging.dbgln

suspend fun <T> PipelineContext<Unit, ApplicationCall>.measureExecTime(block: suspend () -> T): T {
    val start = System.currentTimeMillis()
    val result = block()
    call.application.log.info("[PERFORMANCE] Call ${call.request.uri} took ${System.currentTimeMillis() - start} milliseconds")
    return result
}

suspend fun <T> measureExecTime(callName: String, block: suspend () -> T): T {
    val start = System.currentTimeMillis()
    val result = block()
    dbgln("[PERFORMANCE] Call $callName took ${System.currentTimeMillis() - start} milliseconds")
    return result
}
