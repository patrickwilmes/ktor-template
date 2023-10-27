package boot

import io.ktor.server.application.*
import logging.dbgln
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.MigrationInfoService
import org.jetbrains.exposed.sql.Database

fun Application.connectToDatabase() {
    Database.connect(
        url = configValue(ConfigurationValue.DatabaseUrl),
        user = configValue(ConfigurationValue.DatabaseUser),
        password = configValue(ConfigurationValue.DatabasePassword)
    )
}

fun Application.executeFlywayMigration() {
    val isDatabaseEmpty: (MigrationInfoService) -> Boolean = { it.applied().isEmpty() }
    val flyway = Flyway.configure().dataSource(
        configValue(ConfigurationValue.DatabaseUrl),
        configValue(ConfigurationValue.DatabaseUser),
        configValue(ConfigurationValue.DatabasePassword)
    ).load()
    with(flyway) {
        if (isDatabaseEmpty(info())) {
            dbgln("Database seems to be empty! Raising event ...")
            raise(InvalidateIndicesAndDirectoriesEvent, this@executeFlywayMigration)
        }
        dbgln("Migrating ...")
        migrate()
    }
}
