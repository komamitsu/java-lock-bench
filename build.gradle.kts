buildscript {
    repositories {
        mavenCentral()
    }
}

plugins {
    id("java")
    id("me.champeau.jmh") version "0.7.2"
}

subprojects {
    group = "org.example"
    version = "1.0-SNAPSHOT"

    apply(plugin = "java")
    apply(plugin = "me.champeau.jmh")

    repositories {
        mavenCentral()
    }

    dependencies {
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    }

    jmh {
        warmupIterations.set(4)
        iterations.set(4)
        fork.set(2)
        failOnError.set(true)
        resultFormat.set("CSV")
    }

    tasks.getByName<Test>("test") {
        useJUnitPlatform()
    }
}
