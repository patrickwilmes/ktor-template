package common

fun <T, R> Iterable<T>.mapNotNullToSet(predicate: (T) -> R?): Set<R> {
    val target = mutableSetOf<R>()
    forEach { element ->
        val result = predicate(element)
        if (result != null) {
            target.add(result)
        }
    }
    return target
}

fun <T> Iterable<T>.filterToSet(predicate: (T) -> Boolean): Set<T> {
    val target = mutableSetOf<T>()
    forEach {
        if (predicate(it))
            target.add(it)
    }
    return target
}
