rootProject.name = "db-queue"

include(
    "applications:producer",
    "applications:consumer",
    "components:database-support",
    "databases:messages"
)
