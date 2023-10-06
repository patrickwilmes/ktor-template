plugins {
    applyCommonPlugins()
}

group = "com.bit.lake"
version = "1.0-SNAPSHOT"

detekt {
    buildUponDefaultConfig = true
    allRules = false
    config.setFrom("$rootDir/config/detekt.yml")
}

application {
    mainClass.set("boot.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    implementation(KotlinXDependencies.kotlinXDateTime)
    implementation(KotlinXDependencies.kotlinXCoroutines)
    implementation(project.dependencies.platform(ArrowDependencies.arrowStack))
    implementation(ArrowDependencies.arrowCore)
    implementation(ArrowDependencies.arrowFxCoroutines)
    implementation(ArrowDependencies.arrowFxStm)
    implementation(ArrowDependencies.arrowOptics)
    implementation(ArrowDependencies.arrowSuspendApp)
    implementation(KTorDependencies.ktorServerCorsJVM)
    implementation(KTorDependencies.ktorServerCoreJVM)
    implementation(KTorDependencies.ktorServerContentNegotiationJVM)
    implementation(KTorDependencies.ktorSerializationKotlinXJsonJVM)
    implementation(KTorDependencies.ktorServerNettyJVM)
    implementation(KTorDependencies.ktorServerAuth)
    implementation(Dependencies.logbackClassic)
    implementation(ExposedDependencies.exposedCore)
    implementation(ExposedDependencies.exposedDao)
    implementation(ExposedDependencies.exposedJdbc)
    implementation(ExposedDependencies.exposedKotlinDateTime)
    implementation(Dependencies.flyway)
    implementation(KTorDependencies.ktorNetworkTlsCertificates)
    implementation(Dependencies.koinCore)
    implementation(Dependencies.koinKtor)
    implementation(Dependencies.koinSlf4j)
    implementation(Dependencies.caffeine)
    dokkaGfmPlugin(Dependencies.dokka)

    testImplementation(TestDependencies.assertK)
    testImplementation(TestDependencies.ktorServerTestsJVM)
    testImplementation(TestDependencies.kotlinTestJUnit)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xcontext-receivers")
    }
}

tasks.dokkaHtml.configure {
    dokkaSourceSets {
        configureEach {
            val docsRoot = "$rootDir/docs/packages"
            includes.from()
        }
    }
}
