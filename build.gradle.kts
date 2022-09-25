plugins {
    `java`
}

group "io.github.almogtavor"
version "0.0.1"

repositories {
    mavenCentral()
}

val autoServiceVersion = "1.0.1"

dependencies {
    implementation("com.google.auto.service:auto-service:${autoServiceVersion}")
    annotationProcessor("com.google.auto.service:auto-service:${autoServiceVersion}")
    implementation("com.squareup:javapoet:1.13.0")
    implementation("com.fasterxml.jackson.core:jackson-core:2.13.4")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.4")
    implementation("org.jetbrains:annotations:23.0.0")
    implementation("com.google.guava:guava:31.1-jre")
    implementation("com.sun.codemodel:codemodel:2.6")
    testImplementation("com.google.testing.compile:compile-testing:0.19")
    // Needed as a workaround for using compile-testing with JDK 1.8
    testImplementation(files(org.gradle.internal.jvm.Jvm.current().getToolsJar()))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}