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
