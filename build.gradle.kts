plugins {
    kotlin("jvm") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.1.0"
    application
}

repositories {
    mavenCentral()
    maven { setUrl("https://jitpack.io") }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    implementation(platform("org.http4k:http4k-bom:4.17.7.0"))
    implementation(platform("software.amazon.awssdk:bom:2.17.97"))

    implementation("org.http4k:http4k-core")
    implementation("org.http4k:http4k-format-jackson")
//    implementation("com.zaxxer:HikariCP:5.0.1")
    runtimeOnly("mysql:mysql-connector-java:8.0.25")
    implementation("com.github.oharaandrew314:service-utils:0.5.5")
    implementation("software.amazon.awssdk:dynamodb-enhanced")
    implementation("com.github.oharaandrew314:dynamodb-kotlin-module:0.1.0-beta.3")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.1.0")
    testImplementation("org.http4k:http4k-testing-kotest")
    testImplementation("com.h2database:h2:2.1.210")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

application {
    mainClass.set("com.camcloud.config.camcapi.CamcApiConfigServiceKt")
}

base {
    archivesName.set("CamcloudServer")
}