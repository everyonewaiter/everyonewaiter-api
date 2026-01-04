rootProject.name = "everyonewaiter-api"

pluginManagement {
    val kotlin: String by settings
    val springBoot: String by settings
    val springDependencyManagement: String by settings
    val gitProperties: String by settings
    val spotBugs: String by settings

    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "org.jetbrains.kotlin.jvm" -> useVersion(kotlin)
                "org.jetbrains.kotlin.plugin.spring" -> useVersion(kotlin)
                "org.jetbrains.kotlin.plugin.jpa" -> useVersion(kotlin)
                "org.springframework.boot" -> useVersion(springBoot)
                "io.spring.dependency-management" -> useVersion(springDependencyManagement)
                "com.gorylenko.gradle-git-properties" -> useVersion(gitProperties)
                "com.github.spotbugs" -> useVersion(spotBugs)
            }
        }
    }
}
