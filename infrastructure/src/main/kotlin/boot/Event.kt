package boot

import io.ktor.events.EventDefinition
import io.ktor.server.application.*
import kotlinx.coroutines.runBlocking

val InvalidateIndicesAndDirectoriesEvent: EventDefinition<Application> = EventDefinition()

fun <T> Application.raise(eventDefinition: EventDefinition<T>, value: T): Unit = environment.monitor.raise(eventDefinition, value)
