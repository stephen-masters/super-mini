# AGENTS.md

Guidance for anyone working in this repository with a coding agent (Claude
Code, Codex, Cursor, Gemini CLI or whatever else reads this file) and a fair
summary of the house rules for contributors who use none.

## What this repository is

**super-mini** is the smallest Spring Boot web application that can be
pushed to Cloud Foundry: one controller, one endpoint (`/hello`, which
returns `Hello, World!`) and a `manifest.yml`. It is a starting point and a
smoke test for a Cloud Foundry foundation, not a library or a product.
Nothing is released.

## Hard rules

### Keep it minimal

The point of the project is how little it needs. Do not add dependencies,
endpoints, configuration or files it does not use. The build and CI
hardening below is the one deliberate exception.

### The manifest has to match the build

`manifest.yml` pushes `target/<artifactId>-<version>.jar`. A change to the
artefact id or version in `pom.xml` changes that path in the same commit.
The JRE that `JBP_CONFIG_OPEN_JDK_JRE` asks the Java buildpack for must be
at least `java.version`, and the pinned buildpack release must ship it.

### Stay on current LTS versions

The project tracks the latest LTS JDK (`java.version` in `pom.xml`,
`java-version` in the workflows and the JRE in `manifest.yml`; change them
together) and the latest Spring Boot GA release. Dependency versions come
from the Spring Boot parent's dependency management: do not pin a version
that the parent already manages.

## Layout

| Path | What |
|---|---|
| `src/main/java/.../SuperMiniApp.java` | `@SpringBootApplication`; logs the active profiles and bean names at start-up |
| `src/main/java/.../HelloRestController.java` | The `/hello` endpoint |
| `src/test/java/.../RestEndpointTest.java` | MockMvc tests of the endpoint |
| `manifest.yml` | Cloud Foundry application manifest |
| `.githooks/commit-msg` | Conventional Commits check, shared from `scattercode/release-pipelines` |

## Checks

```bash
./mvnw spotless:apply   # format the Java sources and the POM
./mvnw verify           # everything below, and the tests
```

`./mvnw verify` runs, in order:

- the **enforcer**: the JDK is at least `java.version`, Maven is at least
  3.9, and dependency versions converge;
- the **Spotless** check (palantir-java-format for Java, sortpom for
  `pom.xml`) in the `validate` phase;
- the **compiler** with `-Xlint:all -Werror`, so any warning fails the build;
- the **tests**, and the repackaging of the executable jar.

It needs JDK 25; the Maven wrapper fetches Maven itself. All of it must
pass before a commit. The lefthook hooks run the format check before each
commit and the full build before each push; CI runs the build and a Trivy
scan on every push and pull request.

## Conventions

- British English; sentence case for headings.
- Conventional Commits, enforced by `.githooks/commit-msg` and, for pull
  request titles, by `pr-title.yml`. Dependency updates are `build:`,
  workflow changes `ci:`.
- `.githooks/commit-msg` is a byte-for-byte copy of the template in
  `scattercode/release-pipelines`; CI checks it has not drifted. Change it
  there, not here.
- `main` is protected: changes arrive through pull requests with the CI
  checks passing.
- Hook setup in a fresh clone: `lefthook install`, or without lefthook,
  `git config core.hooksPath .githooks` for the commit message check alone.
