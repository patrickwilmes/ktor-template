package common

import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.application.log
import io.ktor.server.request.uri
import io.ktor.util.pipeline.PipelineContext
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
