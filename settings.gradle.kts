pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "EfffectiveMobileTest"
include(":app")
include(":core:common")
include(":core:model")
include(":core:ui")
include(":core:network")
include(":core:database")
include(":data")
include(":domain")
include(":feature:auth")
include(":feature:home")
include(":feature:favorites")
include(":feature:account")
include(":feature:details")
