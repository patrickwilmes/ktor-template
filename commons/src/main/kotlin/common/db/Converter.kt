package common.db

import common.Sort
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.SortOrder

/**
 * This function takes a mapping function that, from a string based field name of a domain model, converts it into an [Expression],
 * and a [Sort] parameter. Both are used to convert sorting information into an [Array] of [Pair] consisting of an [Expression] to [SortOrder],
 * which is the required format for jetbrains/exposed. The [Array] here is intentional as we are dealing with a fixed size anyway, but also
 * we need to be able to convert the [Array] to a vararg at some point.
 *
 * @param mappingFunc (String) -> Expression<*>, for converting a field name (String) into a jetbrains exposed [Expression<*>] (column name)
 * @param sort [Sort] typealias for a list of pairs, where each pair consists of a field name (as String), and a sort order (as string). This is constructed by [common.http.extractSortParameter].
 *
 * @return [Array<Pair<Expression<*>, SortOrder>>] a representation of the [AbstractTable] columns defined by exposed mapped to the exposed [SortOrder]
 */
fun createSortingParameter(
    mappingFunc: (String) -> Expression<*>,
    sort: Sort,
): Array<Pair<Expression<*>, SortOrder>> {
    val sortOrderStringToSortOrder: (String) -> SortOrder =
        { if (it.lowercase() == "asc") SortOrder.ASC else SortOrder.DESC }
    return sort.map { mappingFunc(it.first) to sortOrderStringToSortOrder(it.second) }
        .toTypedArray()
}
