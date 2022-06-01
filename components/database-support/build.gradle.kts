plugins {
    id("db-queue.java-conventions")
}

val exposedVersion: String by project
val hikariVersion: String by project

dependencies {
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("com.zaxxer:HikariCP:$hikariVersion")
}
