# Project Conventions

Single source of truth for all development standards in this project.
Referenced by `CLAUDE.md` and `.github/copilot-instructions.md`.

---

## Stack

- Java 25, Spring Boot 4.x, Maven
- Spring Data JPA, Spring Security 6, Thymeleaf
- MySQL (dev/prod), H2 (test), Flyway migrations
- Lombok (restricted subset), Jakarta Validation
- JUnit 5, Mockito, AssertJ
- CI: GitHub Actions (`mvn clean verify`)

---

## Architecture

Layered: **Controller -> Service -> Repository**

| Layer      | Responsibility                           | Rules                                                                                                                                                           |
|------------|------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Controller | Receive HTTP requests, delegate, respond | No business logic. No `try/catch`. No entity types in signatures. Always use DTOs. Calls mappers to convert between DTOs and entities/commands.                 |
| Service    | All business logic                       | `@Transactional`. Returns `Optional<T>` for single lookups. Never returns `null`. Never imports from `dto.*`. Accepts entities, primitives, or command objects. |
| Repository | Data access via JPA interfaces           | No business logic. No transactions. No service/controller imports.                                                                                              |
| Entity     | Persistence model                        | No business logic. No presentation logic. No service/controller imports.                                                                                        |
| DTO        | Transfer objects for controller I/O      | Jakarta Validation annotations. No JPA annotations. No entity imports. Lives in `dto/<domain>/request/` or `dto/<domain>/response/`.                            |
| Mapper     | Entity <-> DTO conversion                | Non-instantiable utility class. All methods `public static`. Called only from controllers. No framework dependencies.                                           |
| Command    | Input contract for complex service ops   | Plain POJO in `service/<domain>/`. No validation annotations. No JPA annotations. Used when parameters exceed 3 or map to no single entity.                     |
| Exception  | Domain-specific error types              | Extend `RuntimeException`. Handled exclusively by `@ControllerAdvice`.                                                                                          |
| Validator  | Complex business validation              | Called by services. Throws domain exceptions.                                                                                                                   |

---

## Package Structure

```
tfg.psygcv
├── config/                    # Configuration, security, constants, utilities
│   ├── audit/                 #   JpaAuditConfig
│   ├── constant/              #   RouteConstant
│   ├── security/              #   SecurityConfig, AuthenticatedUser, SuccessHandler
│   └── util/                  #   JsonUtil and other infrastructure utilities
│
├── controller/                # Spring MVC controllers
│   ├── BaseController.java    #   Abstract base (no sub-package for a single class)
│   ├── PublicController.java  #   Public pages (/, /access_denied)
│   ├── appointment/           #   AppointmentController
│   ├── clinic/                #   VeterinaryClinicController, MedicalServiceController
│   ├── dashboard/             #   AdminController, CustomerController,
│   │                          #   ReceptionistController, VeterinarianController
│   ├── medical/               #   MedicalRecordController, VisitController, ReportController
│   ├── pet/                   #   PetController
│   └── user/                  #   UserController (registration/login/admin CRUD), ProfileController
│
├── dto/                       # Request/response DTOs
│   ├── appointment/
│   │   ├── request/           #   ScheduleAppointmentRequest, RescheduleAppointmentRequest
│   │   └── response/          #   AppointmentResponse, AppointmentSummaryResponse
│   ├── clinic/
│   │   ├── request/           #   RegisterClinicRequest, UpdateClinicRequest
│   │   └── response/          #   VeterinaryClinicResponse, VeterinaryClinicSummaryResponse,
│   │                          #   MedicalServiceResponse
│   ├── medical/
│   │   ├── request/           #   CreateMedicalRecordRequest, UpdateMedicalRecordRequest,
│   │   │                      #   CreateVisitRequest, UpdateVisitRequest,
│   │   │                      #   ClinicalExamRequest, AnamnesisRequest,
│   │   │                      #   DiagnosticRequest, TreatmentRequest, VaccineRequest
│   │   └── response/          #   MedicalRecordResponse, MedicalRecordSummaryResponse,
│   │                          #   VisitResponse, ClinicalExamResponse, AnamnesisResponse,
│   │                          #   DiagnosticResponse, TreatmentResponse, VaccineResponse
│   ├── pet/
│   │   ├── request/           #   CreatePetRequest, UpdatePetRequest
│   │   └── response/          #   PetResponse, PetSummaryResponse
│   └── user/
│       ├── request/           #   CreateStaffRequest, UpdateUserRequest
│       └── response/          #   UserResponse, UserSummaryResponse
│
├── entity/                    # JPA entities and enums
│   ├── appointment/
│   ├── audit/                 #   AuditableEntity (shared base)
│   ├── clinic/
│   ├── medical/
│   ├── pet/
│   └── user/
│
├── exception/                 # Domain-specific exceptions + GlobalExceptionHandler
│
├── mapper/                    # Entity <-> DTO mappers
│   ├── appointment/
│   ├── clinic/
│   ├── medical/
│   ├── pet/
│   └── user/
│
├── repository/                # JPA repository interfaces (grouped by domain)
│   ├── appointment/           #   AppointmentRepository, AppointmentQueryRepository,
│   │                          #   AppointmentStatisticsRepository
│   ├── clinic/                #   VeterinaryClinicRepository, MedicalServiceRepository
│   ├── medical/               #   VisitRepository, MedicalRecordRepository,
│   │                          #   MedicalRecordQueryRepository, DiagnosticRepository,
│   │                          #   DiagnosticStatisticsRepository, TreatmentRepository,
│   │                          #   TreatmentStatisticsRepository, AnamnesisRepository,
│   │                          #   ClinicalExamRepository, VaccineRepository
│   ├── pet/                   #   PetRepository
│   └── user/                  #   UserRepository
│
└── service/                   # Business logic (grouped by domain)
    ├── appointment/           #   AppointmentService, AppointmentServiceImpl,
    │                          #   AppointmentValidator
    ├── clinic/                #   VeterinaryClinicService, VeterinaryClinicServiceImpl,
    │                          #   VeterinaryClinicValidator, MedicalServiceService,
    │                          #   MedicalServiceServiceImpl, MedicalServiceValidator
    ├── medical/               #   VisitService, VisitServiceImpl, VisitValidator,
    │                          #   MedicalRecordService, MedicalRecordServiceImpl,
    │                          #   MedicalRecordValidator
    ├── pet/                   #   PetService, PetServiceImpl, PetValidator
    ├── statistics/            #   StatisticsService, StatisticsServiceImpl,
    │                          #   StatisticsValidator
    ├── user/                  #   UserService, UserServiceImpl, UserValidator
    └── validation/            #   BaseValidator (shared abstract base for all validators)
```

### Principles

- Sub-packages group by **domain**: `user/`, `pet/`, `medical/`, `appointment/`, `clinic/`
- Interface, implementation, validator, and command objects for the same service are **co-located** in the same domain
  package
- Cross-cutting base classes live in dedicated shared packages (`entity/audit/`, `service/validation/`)
- Single classes that serve the entire layer live at the layer root (e.g., `BaseController.java`,
  `PublicController.java`)
- DTOs are always split into `request/` and `response/` sub-packages within each domain
- Command objects (`XCommand`) live in `service/<domain>/`, not in `dto/`
- Max nesting: 3 levels
- No catch-all packages: `utils/`, `helpers/`, `misc/`, `common/`, `base/`

---

## Code Style

- Google Java Style, enforced by `checkstyle.xml` in the repository root
- Max line length: 120 characters
- Readability over cleverness
- No suppressed warnings without a comment explaining why

---

## Naming Conventions

| Element                | Convention                         | Example                                               |
|------------------------|------------------------------------|-------------------------------------------------------|
| Classes                | `PascalCase`                       | `AppointmentService`                                  |
| Methods                | `camelCase`                        | `findByClinicId`                                      |
| Constants              | `UPPER_SNAKE_CASE`                 | `MAX_RETRY_COUNT`                                     |
| URL paths              | `kebab-case`                       | `/medical-records/{id}`                               |
| Boolean methods        | `is`, `has`, `can`                 | `isActive()`, `hasAppointments()`                     |
| Service interfaces     | `XService`                         | `UserService`, `PetService`                           |
| Service impls          | `XServiceImpl`                     | `UserServiceImpl`, `PetServiceImpl`                   |
| DTOs (input, CRUD)     | `CreateXRequest`, `UpdateXRequest` | `CreatePetRequest`, `UpdatePetRequest`                |
| DTOs (input, domain)   | `<VerbPhrase>Request`              | `ScheduleAppointmentRequest`, `RegisterClinicRequest` |
| DTOs (output, full)    | `XResponse`                        | `UserResponse`, `AppointmentResponse`                 |
| DTOs (output, summary) | `XSummaryResponse`                 | `PetSummaryResponse`, `UserSummaryResponse`           |
| Mappers                | `XMapper`                          | `UserMapper`, `PetMapper`                             |
| Command objects        | `XCommand`                         | `ScheduleAppointmentCommand`                          |
| Exceptions             | Descriptive noun                   | `DuplicateEmailException`                             |
| Validators             | `XValidator`                       | `AppointmentValidator`                                |

No abbreviations unless universally known (`id`, `url`, `dto`).
No generic names: `XDto`, `XData`, `XInfo`, `XObject`.

---

## Entity Rules

### Structure

- Extend `AuditableEntity` for auditing and soft-delete support
- JPA annotations only (`@Entity`, `@Column`, `@ManyToOne`, etc.)
- Jakarta persistence constraints on columns (`@Column(nullable = false)`, `@Column(unique = true)`)
- No Jakarta validation annotations for business rules (those belong on DTOs)

### equals / hashCode

Every entity must implement `equals()` and `hashCode()` manually, based on the `id` field.
When `id` is `null` (transient entity), fall back to `super.equals()` / `super.hashCode()`.

```java

@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof MyEntity other)) return false;
    return id != null && id.equals(other.getId());
}

@Override
public int hashCode() {
    return getClass().hashCode();
}
```

### Lombok on Entities

**Allowed:** `@Getter`, `@Setter`, `@NoArgsConstructor`

**Forbidden:** `@Data`, `@EqualsAndHashCode`, `@ToString`, `@Builder`

- `@EqualsAndHashCode` generates implementations incompatible with JPA proxy behavior
- `@ToString` risks `LazyInitializationException` and infinite recursion on bidirectional relationships
- `@Builder` conflicts with JPA's requirement for a no-arg constructor and does not work well with inheritance

### Bidirectional Relationships

Synchronization helpers in setters are acceptable only if `equals()` / `hashCode()` are properly implemented.
Prefer managing the owning side explicitly in services rather than relying on setter-based synchronization.

---

## DTO Rules

Controllers never accept or return JPA entities. All controller I/O uses DTOs.

### Naming

**Request DTOs (input):**

- `CreateXRequest`, `UpdateXRequest` for standard CRUD operations
- `<VerbPhrase>Request` for domain-specific operations that don't map to simple create/update semantics:
  `ScheduleAppointmentRequest`, `RescheduleAppointmentRequest`, `RegisterClinicRequest`
- The verb must reflect the domain operation, not the HTTP method

**Response DTOs (output):**

- `XResponse` for full detail views
- `XSummaryResponse` for lists and selectors — only the fields the view actually needs

**Never:** `XDto`, `XData`, `XBean`, `XInfo`, `XObject`

### Validation

- Jakarta Validation annotations (`@NotBlank`, `@Email`, `@FutureOrPresent`, etc.) on DTO fields
- `@Valid` on controller method parameters
- Entities carry only persistence constraints, not business validation

### Mapping

- Manual mapping in dedicated mapper utility classes — one mapper per domain entity
- Mappers are **non-instantiable utility classes**: `private` no-arg constructor that throws
  `UnsupportedOperationException`; all methods are `public static`
- No MapStruct, no reflection-based mappers unless explicitly approved
- Never use `BeanUtils.copyProperties()` — it copies audit fields silently and breaks with entity inheritance
- Mappers are called **only from controllers** — never from services or repositories

**Standard mapper methods:**

| Method                    | Direction    | Use                                        |
|---------------------------|--------------|--------------------------------------------|
| `toEntity(XRequest)`      | DTO → Entity | Before passing to service on create/update |
| `toResponse(Entity)`      | Entity → DTO | Detail views                               |
| `toSummary(Entity)`       | Entity → DTO | Lists and selectors                        |
| `toUpdateRequest(Entity)` | Entity → DTO | Pre-populating edit forms                  |

---

## Service Rules

### Transactions

- `@Transactional(readOnly = true)` at class level for read-heavy services
- `@Transactional` on individual write methods
- Never place `@Transactional` on controllers or repositories

### Return Types

- Single-entity lookups return `Optional<T>` — never return `null`
- Use `orElseThrow(() -> new EntityNotFoundException(...))` for mandatory lookups
- Never call `.get()` on `Optional` without checking

### Ownership Verification

Services that operate on user-owned resources (appointments, pets, visits, medical records) must verify
that the authenticated user has access to the resource before performing the operation.

### Layer Boundaries

Services must not import from the `dto.*` package. DTOs are a presentation concern; importing them
into the service layer couples the domain to the presentation contract and inverts the dependency
direction.

**What services accept as parameters:**

| Scenario                                                         | Service receives                                                | Who does the conversion                 |
|------------------------------------------------------------------|-----------------------------------------------------------------|-----------------------------------------|
| Simple CRUD                                                      | `Entity` (mapped from DTO by controller via mapper)             | Controller                              |
| Few independent params (≤ 3)                                     | Explicit primitives: `Long id`, `LocalDate date`, `String name` | Controller                              |
| Complex operation (> 3 params, or params span multiple entities) | `XCommand` (defined in `service/<domain>/`)                     | Controller maps `XRequest` → `XCommand` |

**Command objects (`XCommand`):**

- Plain POJO — no Jakarta Validation annotations, no JPA annotations, no Spring annotations
- Lives in `service/<domain>/` alongside the service interface and implementation
- Represents the service's own input contract, decoupled from the presentation DTO
- Use `@Builder` (Lombok) is acceptable here — commands are not JPA entities

**Services always return entities.** The controller is responsible for mapping returned entities
to the appropriate response DTO.

---

## Repository Rules

- JPA repository interfaces only — no implementation classes
- Organized by domain (`repository/user/`, `repository/pet/`, etc.)
- Custom queries use `@Query` with JPQL or `@EntityGraph`
- No business logic in repository methods
- No duplicate methods — one method per query, one name per intent

---

## Controller Rules

- Thin: receive request, delegate to service, return view name or redirect
- All input via DTOs with `@Valid`
- All output via DTOs or response objects added to `Model`
- No `try/catch` blocks — exceptions are handled by `@ControllerAdvice`
- No direct entity manipulation — always go through service layer
- Use `@PreAuthorize` for method-level security when URL patterns are insufficient

---

## Exception Handling

### Domain Exceptions

All business errors use domain-specific exception types. Never throw:

- Raw `RuntimeException`
- `IllegalArgumentException` for business rule violations (use a domain exception)
- `jakarta.persistence.EntityNotFoundException` (use the project's own)
- `java.nio.file.AccessDeniedException` (use Spring Security's or a domain exception)

### Global Handler

A single `@ControllerAdvice` (`GlobalExceptionHandler`) handles all exceptions:

- `EntityNotFoundException` -> 404 error page
- `UnauthorizedClinicAccessException` -> 403 error page
- `DuplicateEmailException` -> re-render form with error message
- Unhandled `Exception` -> 500 generic error page with no stack trace exposed

Controllers must not catch exceptions manually. If a controller needs to re-render a form on
validation errors, use `BindingResult` — not `try/catch`.

---

## Security

### URL-Pattern Security

Configured in `SecurityConfig` via `authorizeHttpRequests()`.
Explicitly configure CSRF with `.csrf(Customizer.withDefaults())`.

### Method-Level Security

`@EnableMethodSecurity` is active. Use `@PreAuthorize` in services for:

- Ownership verification (user can only access their own resources)
- Role-based access that is too granular for URL patterns

### Authentication

- Soft-deleted users (`active = false`) must not be able to authenticate
- `AuthenticatedUser.isEnabled()` must return `false` for inactive users
- Spring Security's `UserDetailsService` must reject inactive users explicitly

### Mass Assignment Prevention

Never bind JPA entities directly from form input. Use DTOs with only the fields the form should set.
Services must enforce invariants (e.g., forced roles) regardless of input.

---

## Logging

- SLF4J via Lombok's `@Slf4j` on all service implementations
- Log at service boundaries only — not in entities, not in repositories
- Levels:
    - `INFO`: business events (user created, appointment cancelled, clinic registered)
    - `DEBUG`: technical detail (query parameters, cache hits)
    - `WARN`: recoverable issues (duplicate email attempt, invalid state transition)
    - `ERROR`: unexpected failures (unhandled exceptions, external service failures)
- Never log: passwords, tokens, personal data, full stack traces at INFO level

---

## Database

### Flyway Migrations

- All schema changes managed with Flyway
- Location: `src/main/resources/db/migration/`
- Naming: `V1__description.sql`, `V2__description.sql`
- Never modify an applied migration — create a new one
- `application-test.yml` may use `ddl-auto: create-drop` for H2

### Hibernate

- `ddl-auto: validate` or `none` in dev and prod — never `update` or `create`
- `open-in-view: false` explicitly
- `show-sql: true` only in a dedicated debug profile, never in dev/prod defaults

---

## Spring Profiles

| Profile | Database | DDL           | Flyway   | Notes                    |
|---------|----------|---------------|----------|--------------------------|
| `dev`   | MySQL    | `validate`    | Enabled  | Local development        |
| `test`  | H2       | `create-drop` | Disabled | Fast, isolated test runs |
| `prod`  | MySQL    | `validate`    | Enabled  | Production               |

No hardcoded credentials in any profile. Never modify `application-prod.yml` unless explicitly instructed.

---

## Testing

### What to Test

| Type         | Annotation        | Scope                                             |
|--------------|-------------------|---------------------------------------------------|
| Service unit | Mockito           | Business logic, validation, state transitions     |
| Controller   | `@WebMvcTest`     | Request mapping, validation, security             |
| Repository   | `@DataJpaTest`    | Custom queries, `@SQLRestriction`, `@EntityGraph` |
| Integration  | `@SpringBootTest` | Cross-layer flows (use sparingly)                 |

### What NOT to Test

- Framework internals, getters/setters, trivial mappings
- Lombok-generated code
- Spring Data derived query methods (tested by the framework)

### Mockito

- Constructor injection preferred
- No mocking entities or value objects
- Avoid over-mocking — if everything is mocked, the test proves nothing

### Assertions

- Use AssertJ fluent assertions over JUnit's `assertEquals`
- One logical assertion per test method (multiple `assertThat` calls are fine if they verify the same behavior)

---

## Thymeleaf Templates

- Location: `src/main/resources/templates/`, organized by feature
- Reusable components in `fragments/`
- Base layout: `layouts/layout.html` — all pages extend it
- Use `th:replace` / `th:insert` for shared components
- Always use `th:action` on forms (ensures CSRF token inclusion)
- Use `th:text` for user content — never `th:utext` with unescaped input
- Keep templates thin — prepare all data in controllers via DTOs

---

## Git Conventions

### Commit Messages (Conventional Commits)

Format: `<type>(<scope>): <description>`

Types: `feat`, `fix`, `refactor`, `perf`, `test`, `docs`, `style`, `build`, `ci`, `chore`, `revert`

Rules:

- Imperative mood: "add user registration" not "added"
- Max 72 characters in subject line (aim for 50-60)
- Scope recommended: `(auth)`, `(user)`, `(pet)`, `(medical)`, `(appointment)`, `(clinic)`
- Body explains **why**, not what (encouraged for non-trivial changes)
- No emojis. No vague words: `update`, `WIP`, `changes`, `minor`

### Branch Naming

Format: `<type>/<short-description>`

- `feat/user-profile-avatar`, `fix/appointment-date-validation`, `hotfix/auth-bypass`
- Kebab-case. Prefix matches commit type.

### PR Titles

Conventional Commits format: `feat(user): implement profile page with avatar upload`

---

## Forbidden Actions

- Rewrite large parts of the codebase without explicit approval
- Change the package structure without explicit approval
- Introduce new architectural patterns (CQRS, event sourcing, etc.)
- Add dependencies to `pom.xml` without being asked
- Add Lombok annotations beyond the allowed list
- Silence warnings or skip checks instead of fixing root causes
- Use `ddl-auto: update` or `ddl-auto: create` outside the test profile
- Expose JPA entities through controller signatures
- Catch exceptions in controllers (use `@ControllerAdvice`)
- Return `null` from service methods (use `Optional`)
- Use `BeanUtils.copyProperties()` for entity mapping
- Import from `dto.*` in the service or repository layer
- Call mapper methods from services or repositories — mappers belong in the controller layer
- Log passwords, tokens, or personal data
