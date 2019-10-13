import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val springBootVersion = "2.1.0.RELEASE"
buildscript {
    var kotlinVersion = "1.3.0"
    val springBootVersion = "2.1.0.RELEASE"
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:${springBootVersion}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlinVersion}")
        classpath("org.jetbrains.kotlin:kotlin-allopen:${kotlinVersion}")
    }
}

plugins {
    kotlin("jvm") version "1.3.0"
    "java"
    "kotlin-spring"
    "eclipse"
    "io.spring.dependency-management"
}

repositories {
    mavenCentral()
    jcenter()
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
        kotlinOptions.freeCompilerArgs = listOf("-Xjsr305=strict")
    }

    withType<Test> {
        testLogging.showStandardStreams = true
    }
}

dependencies {
    "org.springframework.boot:spring-boot".let { v ->
        compile("$v-autoconfigure:$springBootVersion") //includes boot 2.0 and autoconfig
        compile("$v-starter-logging:$springBootVersion")
        testCompile("$v-starter-test:$springBootVersion")
    }
    "org.jetbrains.kotlin:kotlin".let { v ->
        compile("$v-stdlib-jdk8")
        compile("$v-reflect")
    }
    compile("org.bouncycastle:bcprov-jdk15on:1.58")
    compile("com.squareup.okhttp3:okhttp:3.10.0")
    //jackson + kotlin
    compile("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.4")

    testCompile("junit:junit:4.8.1")
}
