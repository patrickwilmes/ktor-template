package common.http

import arrow.core.Either
import arrow.core.left
import failure.Failure
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*

suspend fun PipelineContext<Unit, ApplicationCall>.respondWithEntityCreated(path: String) {
    call.response.headers.append("Location", buildUrlForPath(path))
    call.respond(HttpStatusCode.Created)
}

suspend fun PipelineContext<Unit, ApplicationCall>.respondWithServerFailure(origin: Failure): Unit = if (origin is Failure.NotFoundFailure) {
    call.respond(HttpStatusCode.NotFound, origin)
} else {
    call.respond(HttpStatusCode.InternalServerError, origin)
}

suspend fun HttpResponse.checkForFailureOrParseResponse(): Either<Failure, Int> = if (!status.isSuccess()) {
    Failure.BasicFailure(message = "${status}: ${bodyAsText()}").left()
} else {
    Either.catch {
        bodyAsText().toInt()
    }.mapLeft { Failure.BasicFailure("String is not a valid representation of an integer") }
}

