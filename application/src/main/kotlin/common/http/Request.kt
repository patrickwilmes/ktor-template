package common.http

import arrow.core.Either
import common.PageInfo
import common.Sort
import common.measureExecTime
import failure.Failure
import io.ktor.server.application.*
import io.ktor.util.pipeline.*
import kotlinx.serialization.Serializable

fun PipelineContext<Unit, ApplicationCall>.extractSortParameter(): Sort =
    with(call.request.queryParameters) {
        if (contains("sort")) {
            /*
            syntax for query param:
            sort=FIELD || sort=FIELD,SORT_ORDER || sort=FIELD,SORT_ORDER;...
             */
            val fieldsToSort = get("sort")!!.split(";")
            fieldsToSort.map {
                val parts = it.split(",")
                if (parts.size == 2) {
                    Pair(parts[0], parts[1].uppercase())
                } else {
                    Pair(parts[0], "ASC")
                }
            }
        } else {
            emptyList()
        }
    }

suspend fun <T> PipelineContext<Unit, ApplicationCall>.executeBasedOnQueryParam(
    queryParameter: String,
    queryParamPresentFunction: suspend (queryParam: String) -> Either<Failure, T>,
    queryParamNotPresentFunction: suspend () -> Either<Failure, T>,
): Either<Failure, T> {
    with(call.request.queryParameters) {
        if (contains(queryParameter)) {
            return measureExecTime {
                queryParamPresentFunction(get(queryParameter)!!)
            }
        }
        return measureExecTime {
            queryParamNotPresentFunction()
        }
    }
}

@Serializable
data class Page<T>(
    val data: List<T>,
    val paging: PageInfo,
)
