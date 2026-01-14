# Contributing to Order Management Service

Thank you for considering contributing to the Order Management Service! This document provides guidelines and instructions for contributing.

## Code of Conduct

- Be respectful and inclusive
- Focus on constructive feedback
- Help create a positive environment

## Getting Started

1. Fork the repository
2. Clone your fork locally
3. Create a feature branch
4. Make your changes
5. Submit a pull request

## Development Setup

1. Install prerequisites:
   - Java 17
   - Maven 3.8+
   - MySQL 8.0
   - Docker (optional)

2. Set up the development environment:
```bash
cp .env.example .env
# Edit .env with your local configuration
```

3. Run the application:
```bash
mvn spring-boot:run
```

## Coding Standards

### Java Style Guide

- Follow [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- Use Lombok annotations to reduce boilerplate
- Maximum line length: 120 characters
- Use meaningful variable and method names

### Code Organization

```java
// 1. Package declaration
package com.orderservice.service;

// 2. Imports (organized by IDE)
import java.util.*;
import org.springframework.*;
import com.orderservice.*;

// 3. Class Javadoc
/**
 * Service for managing orders.
 */
@Service
public class OrderService {
    // 4. Fields
    private final OrderRepository orderRepository;

    // 5. Constructor
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // 6. Methods (public first, then private)
}
```

### Naming Conventions

- Classes: `PascalCase`
- Methods: `camelCase`
- Constants: `UPPER_SNAKE_CASE`
- Packages: `lowercase`

### Documentation

- Add Javadoc for all public classes and methods
- Include `@param` and `@return` tags
- Explain complex business logic with comments

## Testing Requirements

### Test Coverage

- Minimum 80% code coverage
- Test all business logic thoroughly
- Include edge cases and error scenarios

### Writing Tests

```java
@Test
void methodName_condition_expectedResult() {
    // Arrange
    // Set up test data

    // Act
    // Execute the method

    // Assert
    // Verify the results
}
```

### Test Categories

- Unit tests: Test individual components in isolation
- Integration tests: Test component interactions
- API tests: Test REST endpoints

## Pull Request Process

### Before Submitting

1. Ensure all tests pass:
```bash
mvn clean test
```

2. Format code:
```bash
mvn spring-javaformat:apply
```

3. Run static analysis:
```bash
mvn checkstyle:check
```

4. Update documentation if needed

### PR Guidelines

- Create a descriptive PR title
- Reference related issues
- Provide clear description of changes
- Include screenshots for UI changes
- Keep PRs focused and small
- Respond to review comments promptly

### PR Template

```markdown
## Description
Brief description of changes

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] Breaking change
- [ ] Documentation update

## Testing
- [ ] Unit tests added/updated
- [ ] Integration tests added/updated
- [ ] Manual testing performed

## Checklist
- [ ] Code follows style guidelines
- [ ] Self-review completed
- [ ] Documentation updated
- [ ] Tests pass locally
```

## Branch Naming

- Feature: `feature/description`
- Bug fix: `bugfix/issue-number-description`
- Hotfix: `hotfix/description`
- Release: `release/version`

## Commit Messages

Follow [Conventional Commits](https://www.conventionalcommits.org/):

```
type(scope): subject

body (optional)

footer (optional)
```

Types:
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `style`: Code style changes
- `refactor`: Code refactoring
- `test`: Test additions/changes
- `chore`: Build process or auxiliary tool changes

Examples:
```
feat(orders): add order cancellation endpoint

fix(payment): resolve null pointer exception in payment processing

docs(readme): update API documentation
```

## Issue Reporting

### Bug Reports

Include:
- Description of the bug
- Steps to reproduce
- Expected behavior
- Actual behavior
- Environment details
- Logs/screenshots

### Feature Requests

Include:
- Problem statement
- Proposed solution
- Alternative solutions considered
- Additional context

## Code Review Process

### For Reviewers

- Review within 48 hours
- Provide constructive feedback
- Test the changes locally
- Check for security issues
- Verify documentation

### For Contributors

- Address all review comments
- Ask questions if unclear
- Update PR based on feedback
- Keep discussions professional

## Release Process

1. Create release branch
2. Update version numbers
3. Update CHANGELOG
4. Test thoroughly
5. Merge to main
6. Tag release
7. Deploy to production

## Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [JPA Documentation](https://spring.io/projects/spring-data-jpa)
- [Testing Best Practices](https://spring.io/guides/gs/testing-web/)

## Questions?

- Open a discussion on GitHub
- Contact the maintainers
- Join our Slack channel

Thank you for contributing!
