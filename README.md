# Final Project

## JPA Workshop with Spring Boot

### Authors

- Santiago Valencia - A00395902
- Juan Manuel Díaz - A00394477
- Esteban Gaviria - A00396019

### Description

This project is a backend developed with Spring Boot that allows consuming information from the final project database, Clínica OncoLogic, using Hibernate through Spring Data JPA.

### Technologies Used

- Java 17
- Spring Boot
- Spring Data JPA
- Hibernate
- PostgreSQL
- Docker and Docker Compose
- JUnit (for unit testing)
- JaCoCo (for code coverage measurement)

### Installation and Execution

1. **Clone the repository**

    ```bash
    git clone <REPOSITORY_URL>
    cd <PROJECT_NAME>
    ```

2. **Set up the database with Docker**

    This project uses PostgreSQL as the database. Two environments have been defined in `docker-compose.yml`: one for development (`clinic_dev`) and one for testing (`clinic_test`).

    To start the PostgreSQL containers, run:

    ```bash
    docker-compose up -d
    ```

    This will create two databases:

    - Development: `clinic_dev` (port 5435)
    - Testing: `clinic_test` (port 5436)

3. **Compile and run the application**

    ```bash
    ./mvnw clean install
    ./mvnw spring-boot:run
    ```

    The API will be available at: `http://localhost:8080`

4. **Run tests and generate the report**

    Run the unit tests with:

    ```bash
    ./mvnw test
    ```

    Then, generate the coverage report with:

    ```bash
    ./mvnw jacoco:report
    ```

5. **View the results**

    The coverage report will be generated at:

    `target/site/jacoco/index.html`

    Open this file in a browser to view the code coverage.
