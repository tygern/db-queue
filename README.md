# Database Queue

## Local development

1.  Set up database
    ```shell
    psql < databases/create_databases.sql
    ./gradlew migrate testMigrate
    ```

1.  Run tests
    ```shell
    ./gradlew test
    ```

1.  Run apps
    ```shell
    ./gradlew applications:consumer:run
    ./gradlew applications:producer:run
    ```
