package conf

import com.typesafe.config.ConfigFactory
import io.ktor.server.config.*

object DatabasePool {
    private val appConfig = HoconApplicationConfig(ConfigFactory.load())
}
