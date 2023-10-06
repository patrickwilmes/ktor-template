import org.gradle.kotlin.dsl.PluginDependenciesSpecScope
import org.gradle.kotlin.dsl.application
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.version

private const val KotlinXSerialization = "org.jetbrains.kotlin.plugin.serialization"
private const val ShadowJar = "com.github.johnrengelman.shadow"
private const val KotlinJvm = "jvm"
private const val Dokka = "org.jetbrains.dokka"
private const val Detekt = "io.gitlab.arturbosch.detekt"
private const val Ktlint = "org.jlleitschuh.gradle.ktlint"

fun PluginDependenciesSpecScope.applyCommonPlugins() {
    application
    kotlin(KotlinJvm) version Versions.kotlinVersion
    id(KotlinXSerialization) version Versions.kotlinVersion
    id(ShadowJar) version Versions.shadowJarVersion
    id(Dokka) version Versions.dokkaVersion
    id(Detekt) version Versions.detektVersion
    id(Ktlint) version Versions.ktLintVersion
}
