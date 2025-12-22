import org.gradle.internal.jvm.Jvm
import org.gradle.jvm.toolchain.internal.JavaToolchain
import java.util.*

plugins {
    java
    idea
    application
}

repositories {
    maven(url = "https://maven.aliyun.com/repository/public")
    maven(url = "https://maven.aliyun.com/repository/google")
    maven(url = "https://maven.cleanroommc.com")
    maven(url = "https://repo.gradle.org/gradle/libs-releases-local/")
    maven(url = "https://jitpack.io")
    flatDir {
        dirs("lib")
    }
    mavenCentral()
}

// 加载 mcreator.conf 配置文件
val mcreatorconf = Properties().apply {
    file("src/main/resources/mcreator.conf").inputStream().use { inputStream ->
        this.load(inputStream)
    }
}

group = "rip.sayori.rmcr"
version = mcreatorconf.getProperty("mcreator")
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
        vendor = JvmVendorSpec.MICROSOFT
    }
}

idea {
    module {
        inheritOutputDirs = true
        excludeDirs = setOf(
            file(".gradle"),
            file(".idea"),
            file(".github"),
            file("build"),
            file("gradle"),
            file("jdk"),
            file("license"),
            file("logs")
        )
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "lib", "include" to listOf("*.jar"))))
    implementation("commons-io:commons-io:2.21.0")
    implementation("foxtrot:foxtrot-core:4.0")
    implementation("org.freemarker:freemarker:2.3.31")
    implementation("com.google.code.gson:gson:2.8.9")
    implementation("com.github.sps.junidecode:junidecode:0.3")
    implementation("org.jboss.forge.roaster:roaster-api:2.30.1.Final")
    implementation("org.jboss.forge.roaster:roaster-jdt:2.30.1.Final")
    implementation("com.esotericsoftware.yamlbeans:yamlbeans:1.17")
    implementation("com.fifesoft:rsyntaxtextarea:3.5.3")
    implementation("com.fifesoft:autocomplete:3.3.1")
    implementation("com.fifesoft:languagesupport:3.3.0") {
        exclude(group = "org.mozilla", module = "rhino")
    }
    implementation("org.reflections:reflections:0.10.2")
    implementation("de.javagl:obj:0.3.0")
    implementation("org.apache.logging.log4j:log4j-core:2.24.1")
    implementation("org.apache.commons:commons-text:1.10.0")
    implementation("org.eclipse.jgit:org.eclipse.jgit:7.0.0.202409031743-r")
    implementation("org.slf4j:slf4j-nop:1.7.30")
    implementation("org.gradle:gradle-tooling-api:7.1.1")
    implementation("net.java.balloontip:balloontip:1.2.4.1")
    implementation("com.atlassian.commonmark:commonmark:0.17.0")
    implementation("com.atlassian.commonmark:commonmark-ext-autolink:0.17.0")
    implementation("com.atlassian.commonmark:commonmark-ext-gfm-tables:0.17.0")
    implementation("zone.rong:imaginebreaker:2.1")
    implementation(project(":javafx")) {
        exclude(group = "org.openjfx")
    }
    compileOnly("org.jetbrains:annotations:26.0.1")
}

application {
    mainClass.set("rip.sayori.rmcr.Launcher")
}

distributions {
    main {
        contents {
            from("./plugins"){
                into("lib/plugins/")
            }
            from("src/launcher")
            from(File(tasks.compileJava.get().javaCompiler.get().executablePath.asFile.parent,"..")){
                into("jdk/")
                filePermissions { unix("777") }
            }
            exclude { fte ->
                if(fte.isDirectory && ("legal" in fte.name || "man" in fte.name))return@exclude true
                if(!fte.isDirectory && ".jmod" in fte.name && "base" !in fte.name) return@exclude true
                false
            }
        }
    }
}


