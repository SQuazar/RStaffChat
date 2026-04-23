plugins {
    id("java")
    alias(libs.plugins.shadow)
}

group = "net.nullpointer.rstaffchat"

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

    compileOnly(libs.paper)
    compileOnly(libs.placeholderapi)
    compileOnly(libs.lombok)

    annotationProcessor(libs.lombok)
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

tasks.jar {
    enabled = false
}

tasks.shadowJar {
    archiveBaseName = "RStaffChat"
    archiveVersion = rootProject.version.toString()
    archiveClassifier.set("")

    relocate("io.netty", "net.nullpointer.rstaffchat.shade.netty")
    relocate("io.lettuce", "net.nullpointer.rstaffchat.shade.lettuce")
    relocate("com.google", "net.nullpointer.rstaffchat.shade.google")
//    relocate("org.slf4j", "net.nullpointer.rstaffchat.shade.slf4j")
    relocate("org.reactivestreams", "net.nullpointer.rstaffchat.shade.reactivestreams")
    relocate("reactor", "net.nullpointer.rstaffchat.shade.reactor")
    relocate("redis.clients", "net.nullpointer.rstaffchat.shade.redis")
}

tasks.processResources {
    val props = mapOf("version" to rootProject.version)

    inputs.properties(props)
    filteringCharset = "UTF-8"

    filesMatching("plugin.yml") {
        expand(props)
    }
}

tasks.test {
    useJUnitPlatform()
}