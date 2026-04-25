plugins {
    id("java")
    id("java-library")
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

dependencies {
    api(project(":api"))
    api(libs.gson)
    api(libs.lettuce)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            artifactId = "core"
            version = rootProject.version as String
        }
    }
}

tasks.test {
    useJUnitPlatform()
}