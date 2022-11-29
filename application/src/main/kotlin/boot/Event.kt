package boot

import io.ktor.events.EventDefinition
import io.ktor.server.application.*
import kotlinx.coroutines.runBlocking
import lucene.indexPurgingListener

val InvalidateIndicesAndDirectoriesEvent: EventDefinition<Application> = EventDefinition()

fun <T> Application.raise(eventDefinition: EventDefinition<T>, value: T): Unit = environment.monitor.raise(eventDefinition, value)

fun Application.registerEventHandlers() {
    runBlocking {
        indexPurgingListener()
    }
}
