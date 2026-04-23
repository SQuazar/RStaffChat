plugins {
    id("java")
    id("maven-publish")
}

group = "net.nullpointer.rstaffchat"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            artifactId = "api"
            version = rootProject.version as String
        }
    }
}

tasks.test {
    useJUnitPlatform()
}