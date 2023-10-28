package boot

import io.ktor.server.application.Application
import logging.dbgln
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.MigrationInfoService
import org.jetbrains.exposed.sql.Database

fun Application.connectToDatabase() {
    applyForConfigTriple(
        {},
        { url, user, password ->
            Database.connect(
                url = url,
                user = user,
                password = password,
            )
        },
        configValue(ConfigurationValue.DatabaseUrl),
        configValue(ConfigurationValue.DatabaseUser),
        configValue(ConfigurationValue.DatabasePassword),
    )
}

fun Application.executeFlywayMigration() {
    val isDatabaseEmpty: (MigrationInfoService) -> Boolean = { it.applied().isEmpty() }
    val flyway: Flyway? = applyForConfigTriple(
        {
            null
        },
        { url, user, password ->
            Flyway.configure().dataSource(
                url,
                user,
                password,
            ).load()
        },
        configValue(ConfigurationValue.DatabaseUrl),
        configValue(ConfigurationValue.DatabaseUser),
        configValue(ConfigurationValue.DatabasePassword),
    )
    if (flyway != null) {
        with(flyway) {
            if (isDatabaseEmpty(info())) {
                dbgln("Database seems to be empty! Raising event ...")
                raise(InvalidateIndicesAndDirectoriesEvent, this@executeFlywayMigration)
            }
            dbgln("Migrating ...")
            migrate()
        }
    }
}
