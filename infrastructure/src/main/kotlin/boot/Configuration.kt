package boot

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

fun Application.configValue(configurationValue: ConfigurationValue): String = environment.config.property(configurationValue.key).getString()
fun Application.booleanConfigValue(configurationValue: ConfigurationValue): Boolean =
    configValue(configurationValue).toBoolean()

sealed class ConfigurationValue(val key: String) {
    data object DatabaseUrl : ConfigurationValue("database.url")
    data object DatabaseUser : ConfigurationValue("database.username")
    data object DatabasePassword : ConfigurationValue("database.password")
    data object DevelopmentMode : ConfigurationValue("ktor.development")
}


@OptIn(ExperimentalSerializationApi::class)
fun Application.configure(isDevelopmentMode: Boolean = false) {
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
        allowHeader(HttpHeaders.ContentType)
        if (isDevelopmentMode)
            anyHost()
    }
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
            explicitNulls = false
        })
    }
}
