import org.gradle.api.artifacts.dsl.DependencyHandler

object CoreDependencies {
    val ktorServerCorsJVM = "io.ktor:ktor-server-cors-jvm:${Versions.ktorVersion}"
    val ktorServerCoreJVM = "io.ktor:ktor-server-core-jvm:${Versions.ktorVersion}"
    val ktorServerContentNegotiationJVM =
        "io.ktor:ktor-server-content-negotiation-jvm:${Versions.ktorVersion}"
    val ktorSerializationKotlinXJsonJVM =
        "io.ktor:ktor-serialization-kotlinx-json-jvm:${Versions.ktorVersion}"
    val ktorServerNettyJVM = "io.ktor:ktor-server-netty-jvm:${Versions.ktorVersion}"
    val ktorServerAuth = "io.ktor:ktor-server-auth:${Versions.ktorVersion}"
    val ktorNetworkTlsCertificates = "io.ktor:ktor-network-tls-certificates:${Versions.ktorVersion}"
    val exposedCore = "org.jetbrains.exposed:exposed-core:${Versions.exposedVersion}"
    val exposedDao = "org.jetbrains.exposed:exposed-dao:${Versions.exposedVersion}"
    val exposedJdbc = "org.jetbrains.exposed:exposed-jdbc:${Versions.exposedVersion}"
    val exposedKotlinDateTime =
        "org.jetbrains.exposed:exposed-kotlin-datetime:${Versions.exposedVersion}"
    val kotlinXCoroutines =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutinesVersion}"
    val kotlinXDateTime = "org.jetbrains.kotlinx:kotlinx-datetime:${Versions.kotlinXDateTime}"
    val arrowStack = "io.arrow-kt:arrow-stack:${Versions.arrowVersion}"
    val arrowCore = "io.arrow-kt:arrow-core"
    val arrowFxCoroutines = "io.arrow-kt:arrow-fx-coroutines"
    val arrowFxStm = "io.arrow-kt:arrow-fx-stm"
    val arrowOptics = "io.arrow-kt:arrow-optics"
    val arrowSuspendApp = "io.arrow-kt:suspendapp:${Versions.arrowSuspendApp}"
    val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlinVersion}"
    val logbackClassic = "ch.qos.logback:logback-classic:${Versions.logbackVersion}"
    val flyway = "org.flywaydb:flyway-core:${Versions.flywayVersion}"
    val koinCore = "io.insert-koin:koin-core:${Versions.koinVersion}"
    val koinKtor = "io.insert-koin:koin-ktor:${Versions.koinVersion}"
    val koinSlf4j = "io.insert-koin:koin-logger-slf4j:${Versions.koinVersion}"
    val caffeine = "com.github.ben-manes.caffeine:caffeine:${Versions.caffeineVersion}"
    val dokka = "org.jetbrains.dokka:kotlin-as-java-plugin:${Versions.dokkaVersion}"
}

object TestDependencies {
    val assertK = "com.willowtreeapps.assertk:assertk-jvm:${Versions.assertKVersion}"
    val ktorServerTestsJVM = "io.ktor:ktor-server-tests-jvm:${Versions.ktorVersion}"
    val kotlinTestJUnit = "org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlinVersion}"
    val mockk = "io.mockk:mockk:${Versions.mockkVersion}"
}

fun DependencyHandler.applyKtorEssentials() {
    add("implementation", CoreDependencies.ktorServerCorsJVM)
    add("implementation", CoreDependencies.ktorServerCoreJVM)
    add("implementation", CoreDependencies.ktorServerContentNegotiationJVM)
    add("implementation", CoreDependencies.ktorSerializationKotlinXJsonJVM)
    add("implementation", CoreDependencies.ktorServerNettyJVM)
    add("implementation", CoreDependencies.ktorServerAuth)
    add("implementation", CoreDependencies.ktorNetworkTlsCertificates)
}

fun DependencyHandler.applyArrowEssentials() {
    add("implementation", platform(CoreDependencies.arrowStack))
    add("implementation", CoreDependencies.arrowCore)
    add("implementation", CoreDependencies.arrowFxCoroutines)
    add("implementation", CoreDependencies.arrowFxStm)
    add("implementation", CoreDependencies.arrowOptics)
    add("implementation", CoreDependencies.arrowSuspendApp)
}

fun DependencyHandler.applyExposedEssentials() {
    add("implementation", CoreDependencies.exposedCore)
    add("implementation", CoreDependencies.exposedDao)
    add("implementation", CoreDependencies.exposedJdbc)
    add("implementation", CoreDependencies.exposedKotlinDateTime)
    add("implementation", CoreDependencies.flyway)
}

fun DependencyHandler.applyKoinEssentials() {
    add("implementation", CoreDependencies.koinCore)
    add("implementation", CoreDependencies.koinKtor)
    add("implementation", CoreDependencies.koinSlf4j)
}
