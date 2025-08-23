rootProject.name = "everyonewaiter-api"

pluginManagement {
    val springBoot: String by settings
    val springDependencyManagement: String by settings
    val gitProperties: String by settings
    val spotBugs: String by settings

    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "org.springframework.boot" -> useVersion(springBoot)
                "io.spring.dependency-management" -> useVersion(springDependencyManagement)
                "com.gorylenko.gradle-git-properties" -> useVersion(gitProperties)
                "com.github.spotbugs" -> useVersion(spotBugs)
            }
        }
    }
}
