package failure

import arrow.core.Either
import common.measureExecTime
import kotlinx.serialization.Serializable
import logging.dbgln

sealed interface Failure {

    fun asMessage(): String

    @Serializable
    data class BasicFailure(val message: String) : Failure {
        override fun asMessage(): String = message
    }

    @Serializable
    data class NotFoundFailure(val message: String) : Failure {
        override fun asMessage(): String = message
    }

}

suspend fun <A> trap(
    contextName: String = "defaultContext",
    function: suspend () -> A
): Either<Failure, A> {
    dbgln("Entering trap context $contextName")
    return Either.catch {
        measureExecTime(contextName) {
            function()
        }
    }.mapLeft {
        Failure.BasicFailure(it.message ?: "Failed with unknown reason!")
    }.let {
        dbgln("Exiting trap context $contextName")
        it
    }
}
