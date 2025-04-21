# Final Project

## JPA Workshop with Spring Boot

### Authors

- Santiago Valencia - A00395902
- Juan Manuel Díaz - A00394477
- Esteban Gaviria - A00396019

### Description

This project is a backend developed with Spring Boot that allows consuming information from the final project database, Clínica OncoLogic, using Hibernate through Spring Data JPA.

### Technologies Used

- Java 23
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
    git clone https://github.com/Computacion-2-2025/proyecto-final-siscom.git
    cd proyecto-final-siscom
    ```

2. **Set up the database with Docker**

    This project uses PostgreSQL as the database. Two environments have been defined in `docker-compose.yml`: one for development (`clinic_dev`) and one for testing (`clinic_test`).

    To start the PostgreSQL containers, run:

    ```bash
    docker compose up -d
    ```

    This will create two databases:

    - Development: `clinic_dev` (port 5435)
    - Testing: `clinic_test` (port 5436)

3. **Compile and run the application**

    ```bash
    ./mvnw clean install
    ./mvnw spring-boot:run
    ```

    The API will be available at: `http://localhost:8081`

4. **Run tests and generate the report**

    Run the unit tests with:

    ```bash
    ./mvnw clean test
    ```

    Then, generate the coverage report with:

    ```bash
    ./mvnw jacoco:report
    ```

5. **View the results**

    The coverage report will be generated at:

    `target/site/jacoco/index.html`

    Open this file in a browser to view the code coverage.

### Deployment

The application is deployed on an Apache Tomcat server. You can access it at the following URL:

[http://localhost:8081/g5/siscom/](http://localhost:8081/g5/siscom/)

### Default Users and Passwords

You can log into the web application using the following default credentials:

| Username           | Password            | Role / Description        |
|--------------------|---------------------|----------------------------|
| **admin**          | `admin123`          | Full access (superuser)   |
| **admin2**         | `admin2`            | Full access (superuser)   |
| **doctor1**        | `doctor123`         | Medical personnel         |
| **doctor2**        | `doctor123`         | Medical personnel         |
| **doctor3**        | `doctor123`         | Medical personnel         |
| **patient1**       | `patient123`        | Patient                   |
| **patient2**       | `patient123`        | Patient                   |
| **labtech1**       | `lab123`            | Laboratory technician     |
| **administrative1**| `administrative123` | Administrative staff      |

> **Note:**  
> The users **admin** and **admin2** have full access to all core functionalities, including:

- Log in
- Register new users
- List all users
- Create roles
- Associate permissions to a role
- Assign roles to users
- Delete roles
- Remove roles from users

### Commit Message Guidelines

We follow the **Conventional Commits** standard for writing commit messages.
This ensures a clear and consistent history of changes.

#### Commit Structure

```
type(scope): short description

[optional body]

[optional footer]
```

#### Types

- `feat`: A new feature.
- `fix`: A bug fix.
- `docs`: Documentation changes.
- `style`: Formatting or linting changes.
- `refactor`: Code changes that neither fix a bug nor add a feature.
- `test`: Adding or updating tests.
- `chore`: Maintenance tasks.
- `build`: Changes to the build system or dependencies.
- `ci`: Changes to CI/CD configurations.
- `perf`: Performance improvements.
- `revert`: Reverts a previous commit.

#### Examples

- `feat(auth): add user authentication`
- `fix(api): resolve null pointer exception in user endpoint`
- `docs: update readme with installation instructions`

#### Best Practices

1. Write commit messages in **lowercase**.
2. Keep the short description under **50 characters**.
3. Use the body to explain the **what** and **why** of the change.
4. Reference issues or tickets in the footer (e.g., `closes #123`).
