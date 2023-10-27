package common

import arrow.core.Either
import failure.Failure
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.util.pipeline.*

fun PipelineContext<Unit, ApplicationCall>.strParameter(name: String): Either<Failure, String> =
    Either.catch { call.parameters[name]!! }
        .mapLeft { Failure.BasicFailure(message = "Unable to find parameter: $name") }

suspend inline fun <reified T : Any> PipelineContext<Unit, ApplicationCall>.receiveObject(): Either<Failure, T> =
    Either.catch { call.receive<T>() }.mapLeft {
        Failure.BasicFailure(
            message = "Unable to receive object from request ${it.message}"
        )
    }

fun Int.toBoolean() = this == 1
