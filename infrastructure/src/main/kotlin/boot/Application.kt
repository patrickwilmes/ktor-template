package boot

import arrow.continuations.SuspendApp
import arrow.fx.coroutines.continuations.resource
import io.ktor.server.netty.*
import kotlinx.coroutines.awaitCancellation
import server
import kotlin.time.Duration.Companion.seconds

fun main(): Unit = SuspendApp {
    resource {
        val engine = server(Netty, preWait = 1.seconds).bind()
        engine.application.modules()
    }.use { awaitCancellation() }
}
