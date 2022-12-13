# KTor - Template

This is an opinionated template for building ktor applications in a more functional way. It's loaded with all dependencies required to build production ready
applications:

1. [KTor itself](https://ktor.io/)
2. [ArrowKT](https://arrow-kt.io/)
3. [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization)
4. [Jetbrains/Exposed](https://github.com/JetBrains/Exposed)
5. [Flyway](https://flywaydb.org/)
6. [Lucene](https://lucene.apache.org/)
7. [Koin](https://insert-koin.io/)
8. [Dokka](https://github.com/Kotlin/dokka)

In order to ship the application, the [shadow-jar](https://github.com/johnrengelman/shadow) plugin is in place, allowing you to build a fatjar of the whole application.

## Folder Structure
1. `application`: here goes your source code
2. `buildSrc`: Gradle build functions, mostly used in this project for dependency management
3. `docs`: Documentation

In addition to that we are using some development tools, to streamline the dev workflow a bit:
1. there is a minimal [editorconfig](https://editorconfig.org/) in place for cross editor / ide formatting
2. we have a basic [pre-commit](https://pre-commit.com/) setup, to check things like line feeds before every commit
