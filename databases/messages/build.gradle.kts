plugins {
    id("org.flywaydb.flyway") version "8.5.7"
}

repositories {
    mavenCentral()
}

val flywayMigration by configurations.creating
val postgresVersion: String by project

dependencies {
    flywayMigration("org.postgresql:postgresql:$postgresVersion")
}

flyway {
    configurations = arrayOf("flywayMigration")
}

tasks.register<org.flywaydb.gradle.task.FlywayMigrateTask>("migrate") {
    url = "jdbc:postgresql://localhost:5432/messages?user=messages&password=messages"
}

tasks.register<org.flywaydb.gradle.task.FlywayCleanTask>("dbClean") {
    url = "jdbc:postgresql://localhost:5432/messages?user=messages&password=messages"
}

tasks.register<org.flywaydb.gradle.task.FlywayMigrateTask>("testMigrate") {
    url = "jdbc:postgresql://localhost:5432/messages_test?user=messages&password=messages"
}

tasks.register<org.flywaydb.gradle.task.FlywayCleanTask>("testClean") {
    url = "jdbc:postgresql://localhost:5432/messages_test?user=messages&password=messages"
}
