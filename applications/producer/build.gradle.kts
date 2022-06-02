import org.gradle.api.file.DuplicatesStrategy.INCLUDE

plugins {
    id("db-queue.java-conventions")
}

val logbackVersion: String by project
val exposedVersion: String by project
val postgresVersion: String by project

dependencies {
    implementation(project(":components:database-support"))

    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.postgresql:postgresql:$postgresVersion")

    implementation("ch.qos.logback:logback-classic:$logbackVersion")
}

task<JavaExec>("run") {
    classpath = files(tasks.jar)
    environment("DATABASE_URL", "jdbc:postgresql://localhost:5432/messages?user=messages&password=messages")
}

tasks {
    jar {
        manifest {
            attributes("Main-Class" to "com.example.dbqueue.producer.AppKt")
        }

        duplicatesStrategy = INCLUDE

        from({
            configurations.runtimeClasspath.get()
                .filter { it.name.endsWith("jar") }
                .map {
                    zipTree(it)
                }
        })
    }
}
