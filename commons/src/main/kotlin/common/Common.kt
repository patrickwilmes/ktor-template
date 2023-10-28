package common

import arrow.core.Either
import failure.Failure
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.util.pipeline.PipelineContext

fun PipelineContext<Unit, ApplicationCall>.strParameter(name: String): Either<Failure, String> =
    Either.catch { call.parameters[name]!! }
        .mapLeft { Failure.BasicFailure(message = "Unable to find parameter: $name") }

suspend inline fun <reified T : Any> PipelineContext<Unit, ApplicationCall>.receiveObject(): Either<Failure, T> =
    Either.catch { call.receive<T>() }.mapLeft {
        Failure.BasicFailure(
            message = "Unable to receive object from request ${it.message}",
        )
    }
