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
    return try {
        measureExecTime(contextName) {
            Either.Right(function())
        }
    } catch (e: Throwable) {
        dbgln(contextName, e)
        Either.Left(
            Failure.BasicFailure(
                message = e.message ?: "Unknown reason for error!",
            )
        )
    }.let {
        dbgln("Exiting trap context $contextName")
        it
    }
}
