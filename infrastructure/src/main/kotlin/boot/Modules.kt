package boot

import ConfigurationValue
import booleanConfigValue
import configure
import io.ktor.server.application.*
import org.koin.core.Koin
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

object GlobalKoinContext {
    var koin: Koin? = null

    fun koin() = koin!!
}

fun Application.modules() {
    configure(isDevelopmentMode = booleanConfigValue(ConfigurationValue.DevelopmentMode))
    install(Koin) {
        slf4jLogger()
        val app = modules()
        GlobalKoinContext.koin = app.koin
    }
    connectToDatabase()
    executeFlywayMigration()
    installRoutes()
}

private fun Application.installRoutes() {
}
