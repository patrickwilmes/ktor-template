package functions

fun <T> Iterable<T?>.filterNotNullToSet(): Set<T> {
    val result = mutableSetOf<T>()
    forEach { element ->
        if (element != null) {
            result.add(element)
        }
    }
    return result
}

fun <T, R> Iterable<T>.mapToSet(predicate: (T) -> R): Set<R> {
    val target = mutableSetOf<R>()
    forEach { element ->
        target.add(predicate(element))
    }
    return target
}
