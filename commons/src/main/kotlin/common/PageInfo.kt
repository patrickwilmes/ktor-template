package common

import common.db.ResultList
import common.http.Page
import common.http.buildUrlForPath
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.util.pipeline.PipelineContext
import kotlinx.serialization.Serializable

private const val DEFAULT_PAGE_SIZE = 10

fun <T> PipelineContext<Unit, ApplicationCall>.assemblePage(
    path: String,
    resultList: ResultList<T>,
): Page<T> {
    val previousPage: (Int) -> Int = { if (it == 0) 0 else it - 1 }
    val nextPage: (Int, Int) -> Int =
        { current, total -> if (current >= total) current else current + 1 }
    val page = call.request.queryParameters["page"]?.toInt() ?: 0
    val size = call.request.queryParameters["size"]?.toInt() ?: DEFAULT_PAGE_SIZE
    return Page(
        data = resultList.data,
        paging = PageInfo(
            totalPages = resultList.totalPages.toInt(),
            totalElements = resultList.totalElements.toInt(),
            page = page,
            size = size,
            prev = "${buildUrlForPath(path)}?page=${previousPage(page)}&size=$size",
            next = "${buildUrlForPath(path)}?page=${nextPage(page, resultList.totalPages.toInt())}&size=$size",
        ),
    )
}

@Serializable
data class PageInfo(
    val totalPages: Int,
    val totalElements: Int,
    val page: Int,
    val size: Int,
    val prev: String,
    val next: String,
)
