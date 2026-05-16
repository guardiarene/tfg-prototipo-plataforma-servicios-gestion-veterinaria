# Copilot Instructions

Full standards in `CONVENTIONS.md` (project root). This file is a compact reference.

## Stack

Java 25, Spring Boot 4.x, Spring Data JPA, Spring Security 6, Thymeleaf, MySQL/H2, Flyway.

## Architecture

Controller (thin, no logic) -> Service (all business logic, `@Transactional`) -> Repository (JPA interfaces).

## DTOs (mandatory for all controller I/O)

- Input: `CreateXRequest`, `UpdateXRequest` with `jakarta.validation` annotations
- Output: `XResponse`, `XSummaryResponse`
- Controllers use `@Valid` on all request DTOs
- Mapping: manual mapper classes or static factory methods — no MapStruct, no `BeanUtils.copyProperties()`
- Never expose JPA entities in controller signatures

## Entities

- Extend `AuditableEntity`. Implement `equals`/`hashCode` manually based on `id`
- Allowed Lombok: `@Getter`, `@Setter`, `@NoArgsConstructor`
- Forbidden: `@Data`, `@EqualsAndHashCode`, `@ToString`, `@Builder`

## Services

- `@Transactional(readOnly = true)` at class level, `@Transactional` on write methods
- Return `Optional<T>` for single lookups — never return `null`
- Verify resource ownership before operating on user-owned data

## Exceptions

- Domain-specific exceptions only — never `RuntimeException`, `IllegalArgumentException`, or JPA exceptions
- Single `@ControllerAdvice` handles all exceptions — controllers must not `try/catch`

## Naming and Packages

- Interfaces: `XService` (not `XServiceInterface`). Implementations: `XServiceImpl`
- All layers use domain sub-packages: `user/`, `pet/`, `medical/`, `appointment/`, `clinic/`
- Service interface + implementation + validator co-located in the same domain package
- Repository base + query + statistics co-located in the same domain package
- `controller/dashboard/` for role-based dashboards, `controller/user/` for user operations

## Logging

`@Slf4j` on services. SLF4J only. Never log passwords or personal data.

## Testing

Service logic with Mockito. Controllers with `@WebMvcTest`. Repositories with `@DataJpaTest`.

## Do NOT

- Add dependencies without being asked
- Put business logic in controllers
- Expose entities in controller signatures
- Catch exceptions in controllers
- Use `ddl-auto: update` outside test profile
- Introduce new architectural patterns
- Return `null` from service methods
