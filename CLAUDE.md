# CLAUDE.md

Read `CONVENTIONS.md` before starting any task. It is the single source of truth for all project standards.

## Commands

```bash
mvn clean verify                                    # Full build (checkstyle + tests) — run before completing any task
mvn test                                            # Tests only
mvn test -Dtest=ClassName                           # Single test class
mvn checkstyle:check                                # Style check only
mvn spring-boot:run -Dspring-boot.run.profiles=dev  # Local dev server
```

## Workflow

### Before Writing Code

1. Read `CONVENTIONS.md` if you haven't this session
2. Read existing files in the affected domain (entity, repository, service, DTO, mapper, controller)
3. Identify established patterns — naming, structure, error handling
4. Follow those patterns exactly — do not introduce new conventions

### After Writing Code

1. `mvn checkstyle:check` — fix all violations
2. `mvn test` — all tests must pass
3. `mvn clean verify` — must pass before marking any task complete

## Common Tasks

### Implementing a Feature

1. Identify affected layers: entity -> repository -> service -> DTO/mapper -> controller
2. Read existing code in the same domain to match patterns
3. Implement bottom-up: entity -> repository -> service -> DTO/mapper -> controller
4. Write tests for each layer (service tests mandatory, controller tests for new endpoints)
5. Run `mvn clean verify`

### Adding an Entity

1. Read an existing entity — replicate the `equals`/`hashCode` pattern
2. Create entity in `<domain>/entity/` with JPA annotations
3. Create repository in `<domain>/repository/`
4. Create DTOs in `<domain>/dto/`: `CreateXRequest`, `UpdateXRequest`, `XResponse`
5. Create mapper in `<domain>/mapper/`
6. Create command objects in `<domain>/command/` if needed
7. Create service interface and implementation in `<domain>/service/`
8. Create controller in `<domain>/controller/` using DTOs with `@Valid`
9. Add domain-specific exceptions in `exception/` if needed
10. Create Flyway migration in `src/main/resources/db/migration/`
11. Write tests — run `mvn clean verify`

### Fixing a Bug

1. Read the related code first
2. Write a failing test that reproduces the bug (when possible)
3. Implement the fix
4. Verify the test passes
5. Run `mvn clean verify`

## Critical Rules

- **Never** modify code without reading existing files in the same domain first
- **Never** add dependencies to `pom.xml` unless explicitly asked
- **Never** expose JPA entities through controllers — always use DTOs
- **Never** put business logic in controllers
- **Never** catch exceptions in controllers — `@ControllerAdvice` handles them
- **Never** modify `application-prod.yml` unless explicitly instructed
- **Never** use `BeanUtils.copyProperties()` — map fields explicitly
- **Always** run `mvn clean verify` before considering a task complete
- **Always** follow existing patterns over inventing new ones

## When Uncertain

If a pattern is ambiguous or inconsistent in the codebase, ask before proceeding.
Do not guess. Do not silently pick one pattern over another.
