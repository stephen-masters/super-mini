Super mini
==========

The smallest Spring Boot web application that runs on Cloud Foundry. It has one endpoint:

    $ curl http://localhost:8080/hello
    Hello, World!

At start-up it logs the active profiles and the names of every bean in the application context,
which shows at a glance what a push has switched on.

It needs Java 25 and uses Spring Boot 4. The Maven wrapper downloads Maven itself, so to run a
full build, including the format check and the tests:

    ./mvnw clean verify

To run it locally, on port 8080:

    ./mvnw spring-boot:run

To push it to Cloud Foundry, build the jar and push from the repository root, where
[`manifest.yml`](manifest.yml) names the jar, the memory and the buildpack:

    ./mvnw package
    cf push

The manifest pins the [Cloud Foundry Java buildpack](https://github.com/cloudfoundry/java-buildpack)
to v5.1.1, which ships a Java 25 JRE, and selects it with `JBP_CONFIG_OPEN_JDK_JRE`, because the
buildpack's default JRE is 17.

If the format check fails, `./mvnw spotless:apply` fixes it. To install the git hooks, which
check formatting before each commit and run the build before each push:

    lefthook install

See [AGENTS.md](AGENTS.md) for the conventions and [SECURITY.md](SECURITY.md) for reporting a
vulnerability.

Licensed under the [MIT License](LICENSE): copy and adapt it freely.
