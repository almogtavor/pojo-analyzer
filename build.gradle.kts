plugins {
    java
    `maven-publish`
    signing
}

group = "io.github.almogtavor"
version = "1.3.0"

repositories {
    mavenCentral()
}

val autoServiceVersion = "1.0.1"

dependencies {
    implementation("com.google.auto.service:auto-service:${autoServiceVersion}")
    annotationProcessor("com.google.auto.service:auto-service:${autoServiceVersion}")
    implementation("com.squareup:javapoet:1.13.0")
    implementation("com.fasterxml.jackson.core:jackson-core:2.13.4")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.4.2")
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

java {
    withJavadocJar()
    withSourcesJar()
}

val sonatypeUsername: String by project
val sonatypePassword: String by project

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = project.group as String?
            artifactId = project.name
            version = project.version as String?
            from(components["java"])
            pom {
                name.set("POJO Analyzer")
                description.set("A library for generating details of POJOs at compile time")
                url.set("https://github.com/almogtavor/pojo-analyzer")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("almogtavor")
                        name.set("Almog Tavor")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/almogtavor/pojo-analyzer.git")
                    developerConnection.set("scm:git:https://github.com/almogtavor/pojo-analyzer.git")
                    url.set("https://github.com/almogtavor/pojo-analyzer/")
                }
            }
            repositories {
                maven {
                    val releasesRepoUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
                    url = uri(releasesRepoUrl)
                    credentials {
                        username = sonatypeUsername
                        password = sonatypePassword
                    }
                }
            }
        }
    }
}

tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
}

signing {
    sign(publishing.publications["mavenJava"])
}