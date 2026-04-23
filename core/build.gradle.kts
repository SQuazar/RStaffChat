plugins {
    id("java")
    id("java-library")
    id("maven-publish")
}

group = "net.nullpointer.rstaffchat"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    api(project(":api"))
    implementation("com.google.code.gson:gson:2.13.2")
    implementation("io.lettuce:lettuce-core:7.5.1.RELEASE")

    compileOnly("org.projectlombok:lombok:1.18.44")
    annotationProcessor("org.projectlombok:lombok:1.18.44")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            artifactId = "core"
        }
    }
}

tasks.test {
    useJUnitPlatform()
}