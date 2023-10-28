package koin

import org.koin.core.Koin

object GlobalKoinContext {
    var koin: Koin? = null

    fun koin() = koin!!
}
