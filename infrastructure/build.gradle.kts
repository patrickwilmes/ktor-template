plugins {
    applyApplicationPlugins()
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
    applyKtorEssentials()
    applyArrowEssentials()
    applyKoinEssentials()
    applyExposedEssentials()
    implementation(CoreDependencies.kotlinXDateTime)
    implementation(CoreDependencies.kotlinXCoroutines)
    implementation(CoreDependencies.logbackClassic)
    implementation(CoreDependencies.caffeine)
    dokkaGfmPlugin(CoreDependencies.dokka)

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
