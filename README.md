# Final Project

## Commit Message Guidelines

We follow the **Conventional Commits** standard for writing commit messages.
This ensures a clear and consistent history of changes.

### Commit Structure

```
type(scope): short description

[optional body]

[optional footer]
```

### Types

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

### Examples

- `feat(auth): add user authentication`
- `fix(api): resolve null pointer exception in user endpoint`
- `docs: update readme with installation instructions`

### Best Practices

1. Write commit messages in **lowercase**.
2. Keep the short description under **50 characters**.
3. Use the body to explain the **what** and **why** of the change.
4. Reference issues or tickets in the footer (e.g., `closes #123`).

