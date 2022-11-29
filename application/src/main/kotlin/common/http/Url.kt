package common.http

import io.ktor.server.application.*
import io.ktor.util.pipeline.*

fun PipelineContext<Unit, ApplicationCall>.buildUrlForPath(path: String) = "${assembleHostUrlPart()}$path"
fun PipelineContext<Unit, ApplicationCall>.assembleHostUrlPart() = with(call.request.local) {
    "$scheme://$host:$port"
}
