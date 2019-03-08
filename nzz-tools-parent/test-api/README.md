# Senior Java Test Library

This library provides base and utility classes for tests and ways to ease test writting.

### Features:

- Base classes;
- Mocking with Mockito Framework;
- Automatic mocking with annotations;
- Utility methods;
- Shorthand methods.

### Base classes:

- `UnitTest`: Base class for unit tests.
- `IntegrationTestJspare`: Base class for integration* tests with Vert.x and Jspare (provided dependency).
- `IntegrationTestSpring`: Base class for integration* tests with Spring Framework (provided dependency).

<small>* except data base configuration/comunication.</small>


## Vert.x + Jspare

### Setting up:

In order to test an Jspare application, you must specify the application's `io.vertx.core.Verticle` class, as follows:

```java
import br.com.senior.test.TestVerticle;
import br.com.senior.test.IntegrationTestJspare;

import your.app.domain.AppVerticle.class

@TestVerticle(AppVerticle.class)
public class JspareTest extends IntegrationTestJspare {
}
```


### Registering/accessing mocked instances to/from Jspare's Dependency Injection context

You can define and get an object instance provided by Jspare's Dependency Injection context, useful to override a real instance with a mocked one. This can be done with the following command:

```java
Object providedInstance = JspareIntegrationTestDIManager.getMockedInstance(Object.class);
```
This DI management is used to automatic mock repositories, as integration with database is not yet implemented.

Inside test classes, you can get an repository mocked instance simply declaring the field with `org.jspare.unit.mock.Mock` annotation.

```java
// omitted imports [...]

import org.jspare.unit.mock.Mock;

@TestVerticle(AppVerticle.class)
public class JspareTest extends IntegrationTestJspare {

    @Mock
    private YourRepository repository;

}
```

### Examples:

- [br.com.senior.volkswagen.routes.MetricRouteTest.java][MetricRouteTest]

[MetricRouteTest]: http://git.senior.com.br/software-sob-medida/volkswagen-riscos-ergonomicos/blob/master/volkswagen-api/src/test/java/br/com/senior/volkswagen/routes/MetricRouteTest.java
