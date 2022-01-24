plugins {
    kotlin("jvm") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.1.0"
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    implementation(platform("org.http4k:http4k-bom:4.17.9.0"))

    implementation("org.http4k:http4k-format-jackson")
    implementation("mysql:mysql-connector-java:8.0.25")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.1.0")
    testImplementation("org.http4k:http4k-testing-kotest")
    testImplementation("com.h2database:h2:2.1.210")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("dev.andrewohara.petstore.RunnerKt")
}