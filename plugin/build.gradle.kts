plugins {
    id("java")
    id("com.gradleup.shadow") version "9.4.1"
}

group = "net.nullpointer.rstaffchat"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        name = "papermc-repo"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        name = "sonatype"
        url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
    }
    maven {
        name = "extendedclip"
        url = uri("https://repo.extendedclip.com/releases/")
    }
}

dependencies {
    implementation(project(":core"))

    compileOnly("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.12.2")
    compileOnly("org.projectlombok:lombok:1.18.44")

    annotationProcessor("org.projectlombok:lombok:1.18.44")
}

val targetJavaVersion = 17

java {
    val version = JavaVersion.toVersion(targetJavaVersion)
    if (JavaVersion.current() < version)
        toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) {
        options.release.set(targetJavaVersion)
    }
}

tasks.shadowJar {
    relocate("io.netty", "net.nullpointer.rstaffchat.shade.netty")
    relocate("io.lettuce", "net.nullpointer.rstaffchat.shade.lettuce")
    relocate("com.google", "net.nullpointer.rstaffchat.shade.google")
//    relocate("org.slf4j", "net.nullpointer.rstaffchat.shade.slf4j")
    relocate("org.reactivestreams", "net.nullpointer.rstaffchat.shade.reactivestreams")
    relocate("reactor", "net.nullpointer.rstaffchat.shade.reactor")
    relocate("redis.clients", "net.nullpointer.rstaffchat.shade.redis")
}

tasks.processResources {
    val props = mapOf("version" to project.version)

    inputs.properties(props)
    filteringCharset = "UTF-8"

    filesMatching("plugin.yml") {
        expand(props)
    }
}

tasks.test {
    useJUnitPlatform()
}