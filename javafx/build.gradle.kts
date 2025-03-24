plugins {
    id("java")
}

group = "rip.sayori.rmcr"
version = "11"

repositories {
    maven("https://maven.aliyun.com/repository/public")
}

val deps = arrayOf("base","controls","graphics","media","swing","web")
val platforms = arrayOf("win","linux","mac")

dependencies {
    deps.forEach { name ->
        platforms.forEach { plat ->
            implementation("org.openjfx:javafx-$name:$version:$plat")
        }
    }
}

tasks.jar{
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    configurations.runtimeClasspath.get().forEach {
        if(it.isDirectory())
            from(it)
        else
            from(zipTree(it))
    }
}