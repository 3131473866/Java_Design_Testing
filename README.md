# Java Design & Testing Practice

Two small Java projects that explore how to structure and test object-oriented code: **dependency injection with aspect-oriented programming** using Spring Boot, and **build automation with mock-based unit testing** using Gradle, JUnit 5 and JMockit.

| Project | Focus | Stack |
| --- | --- | --- |
| [`di-and-aop`](di-and-aop) | Constructor injection, profile-based bean wiring, logging aspect | Java 17, Spring Boot 3.3, Spring AOP |
| [`automation-and-mocking`](automation-and-mocking) | Mocking a database interface, coverage, custom Gradle task | Java 17, JUnit 5, JMockit, JaCoCo |

Each project is self-contained with its own Gradle wrapper, so you can `cd` into either one and run it without installing Gradle. Only a JDK 17+ is required.

---

## 1. `di-and-aop`: Dependency Injection and AOP

A conceptual "web application" whose components are wired together by Spring rather than constructed by hand. It is not a real web server; each service just prints a line when it runs, which makes the dependency graph and the aspect easy to observe.

### Dependency graph

```
WebApplication
└── WebServer
    ├── Frontend ────────── Authentication
    ├── Middleware
    └── Persistence ─┬───── FileSystem
                     └───── Connection
```

Every service has two interchangeable implementations:

| Interface | Standard (`task1`) | Alternative (`task2`) |
| --- | --- | --- |
| `Frontend` | `FrontendHTML` | `FrontendGWT` |
| `Middleware` | `MiddlewareTomcat` | `MiddlewareJBoss` |
| `Persistence` | `PersistenceMySQL` | `PersistenceOracle` |
| `Authentication` | `AuthenticationSSL` | `AuthenticationTSL` |
| `FileSystem` | `FileSystemNTFS` | `FileSystemNFS` |
| `Connection` | `ConnectionPooled` | `ConnectionJDBC` |

### What it demonstrates

- **Constructor injection.** Every dependency is passed through the constructor and stored in a `final` field. Each `run()` method calls the services it depends on.
- **Profile-based configuration.** `StandardConfig` (active under the `task1` profile) and `AdditionalConfig` (active under `task2`) each declare a full set of `@Bean` definitions. Switching profiles swaps the whole implementation stack without touching any application code.
- **Aspect-oriented logging.** `LoggingAspect` uses `@Before` advice to intercept every method call on the `Frontend*`, `Middleware*` and `Persistence*` implementations and print `Logging Aspect: <QualifiedClassName>.<methodName>`. The aspect is a `@Component`, so it is active under both profiles.

### Run it

```shell
cd di-and-aop

./gradlew build                                              # compile and run the tests
./gradlew bootRun --args="--spring.profiles.active=task1"    # standard stack
./gradlew bootRun --args="--spring.profiles.active=task2"    # alternative stack
```

Running `bootRun` with no arguments uses `task2`, the default set in `build.gradle`. On Windows use `gradlew.bat` in place of `./gradlew`.

The tests in `TaskOneTests` and `TaskTwoTests` start the full Spring context under each profile, so a missing or ambiguous bean fails the build with a `NoSuchBeanDefinitionException` or `UnsatisfiedDependencyException`.

---

## 2. `automation-and-mocking`: Build Automation and Mocking

Unit tests for `UserAdmin`, a class that manages users through a `DBConnection` interface. Because the real database is out of scope, the interface is mocked with JMockit, which lets each test control exactly what the "database" returns or throws.

### What it demonstrates

- **Mock-based testing.** `UserAdminTest` uses `@Mocked`, `Expectations` and `Verifications` to cover every branch of `UserAdmin`:
  - `createUser`: success, duplicate username, and `SQLException` from both `userExists` and `addUser`.
  - `removeUser`: success, missing user, attempt to remove an administrator, and `SQLException` from both `userExists` and `deleteUser`.
  - `runUserReport`: empty database, 1 / 5 / 10 users (detailed listing), 11 / 15 users (summary with the "N more..." line), and `SQLException`.
- **Interaction verification.** Tests assert not only return values and console output, but also that forbidden calls never happen, for example that `deleteUser` is never invoked for an administrator.
- **Code coverage.** JaCoCo is wired into `check`, so the coverage report is generated automatically.
- **Custom Gradle task.** `myGradleTask` depends on `build` and then prints `Build Finished!`.

### Run it

```shell
cd automation-and-mocking

./gradlew build              # compile, run tests, generate coverage
./gradlew -q myGradleTask    # build, then print "Build Finished!"
```

Open `build/reports/jacoco/test/html/index.html` in a browser to view the coverage report.

---

## Repository layout

```
.
├── di-and-aop/                 # Spring Boot DI + AOP project
│   └── src/{main,test}/java/com/example/diaop/
├── automation-and-mocking/     # JUnit + JMockit + Gradle project
│   └── src/{main,test}/java/com/example/mocking/
└── .github/workflows/ci.yml    # builds and tests both projects on push and PR
```

## Continuous integration

GitHub Actions builds and tests both projects on every push and pull request to `main`, and uploads the JaCoCo coverage report for the mocking project as a build artifact.
