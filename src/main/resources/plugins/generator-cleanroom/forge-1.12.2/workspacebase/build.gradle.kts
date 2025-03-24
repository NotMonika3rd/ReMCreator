plugins {
    id("java")
    id("eclipse")
    id("xyz.wagyourtail.unimined") version "1.3.13"
}

group = "com.example.modid"
version = "1.0"

unimined.minecraft {
    version("1.12.2")

    mappings {
        mcp("snapshot", "20171003-1.12")
    }

    cleanroom {
        loader("0.3.0-alpha")
    }
}
configurations{
    implementation.get().extendsFrom(configurations["minecraft"])
    implementation.get().extendsFrom(configurations["minecraftLibraries"])
    all{
        exclude(group = "org.lwjgl.lwjgl")
        exclude(group = "net.java.dev.jna", module = "platform")
    }
}

tasks.jar{
    archiveFileName = "modid-1.0.jar"
}

repositories {
    mavenCentral()
    unimined.cleanroomRepos()
}

dependencies {

}

apply{
    from("mcreator.gradle")
}