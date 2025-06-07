# NexSys Test Suite Documentation

This document provides comprehensive documentation for the NexSys test suite, covering all unit tests for the utility packages and core functionality.

## Test Framework

The NexSys project uses the following testing stack:
- **JUnit 5 (Jupiter)** - Primary testing framework
- **AssertJ** - Fluent assertions library
- **Mockito** - Mocking framework for unit tests
- **Gradle** - Test execution and reporting

## Test Structure

```
src/test/java/com/nexsys/
└── util/
    ├── CollectionUtilsTest.java
    ├── ColorUtilsTest.java
    ├── DateTimeUtilsTest.java
    ├── EventTypeTest.java
    ├── FileUtilsTest.java
    ├── HttpClientUtilsTest.java
    ├── ImageUtilsTest.java
    ├── JsonUtilsTest.java
    ├── LanguageUtilsTest.java
    ├── LimitedSizeMapTest.java
    ├── LoggingUtilsTest.java
    ├── LoopGuardTest.java
    ├── NetworkUtilsTest.java
    ├── NexsysExceptionTest.java
    ├── NexsysExecutorTest.java
    ├── NormalizedNameRegistryTest.java
    ├── PackageUtilsTest.java
    ├── PercentageHelperTest.java
    ├── PercentageTest.java
    ├── ProcessUtilsTest.java
    ├── ScalingUtilsTest.java
    ├── SignalTypeTest.java
    ├── SslUtilsTest.java
    ├── StatisticsUtilsTest.java
    ├── SystemInfoTest.java
    ├── TemperatureUtilsTest.java
    ├── ThreadUtilsTest.java
    ├── TimeoutUtilsTest.java
    ├── UlidUtilsTest.java
    ├── UnitConversionTest.java
    ├── UnitSystemTest.java
    └── UuidUtilsTest.java
```

## Running Tests

### Command Line (Git Bash)
```bash
# Run all tests
cd ~/Path/To/Your/Repos/NexSys
./gradlew test

# Run specific test class
./gradlew test --tests com.nexsys.util.PercentageTest

# Run with detailed output
./gradlew test --info

# Run tests matching pattern
./gradlew test --tests "*Language*"
```

### IntelliJ IDE
- Right-click on test class/method → Run
- Use Ctrl+Shift+F10 to run current test
- View results in the Test Results window

## Key Test Classes

### PercentageTest
Tests the `Percentage` value object:
```java
@Test
void testValidPercentage() {
    Percentage p = Percentage.of(50);
    assertEquals(50, p.value());
    assertEquals(0.5, p.asFraction(), 0.001);
}

@ParameterizedTest
@ValueSource(ints = {-1, -100, 101, 200})
void testInvalidRange(int value) {
    assertThrows(IllegalArgumentException.class, () -> Percentage.of(value));
}
```

### PercentageHelperTest
Tests percentage calculation utilities:
```java
@Test
void testOrderedListItemToPercentage() {
    List<String> items = List.of("low", "medium", "high");
    Percentage p = PercentageHelper.orderedListItemToPercentage(items, "medium");
    assertEquals(66, p.value()); // 2/3 = 66%
}
```

### LanguageUtilsTest
Comprehensive language matching tests using nested test classes:
```java
@Nested
@DisplayName("isLanguageMatch")
class IsLanguageMatchTest {
    @Test
    void testNorwegianAliases() {
        assertTrue(LanguageUtils.isLanguageMatch("nb", "no"));
        assertTrue(LanguageUtils.isLanguageMatch("no", "nb"));
    }
}

@Nested
@DisplayName("Dialect matching")
class DialectTest {
    @Test
    void testTwoPartDialect() {
        Dialect dialect = new Dialect("en-US");
        assertEquals("en", dialect.getLanguage());
        assertEquals("US", dialect.getRegion());
    }
}
```

### NexsysExceptionTest
Tests the exception framework:
```java
@Test
public void testDeviceNotFoundException() {
    String deviceId = "sensor.bedroom";
    NexsysException exception = NexsysException.deviceNotFound(deviceId);
    
    assertEquals(ErrorCategory.DEVICE, exception.getCategory());
    assertEquals("DEVICE_NOT_FOUND", exception.getErrorCode());
    assertTrue(exception.hasContext("device_id"));
    assertEquals(3, exception.getSuggestions().size());
}
```

### UuidUtilsTest
Tests UUID generation with uniqueness verification:
```java
@Test
void testUniqueness() {
    Set<String> uuids = new HashSet<>();
    for (int i = 0; i < 10_000; i++) {
        uuids.add(UuidUtils.randomUuidHex());
    }
    assertEquals(10_000, uuids.size(), "All generated UUIDs should be unique");
}
```

### SignalTypeTest
Tests the generic signal type wrapper:
```java
@Test
void testFormat() {
    SignalType<String> signal = SignalType.format("signal_%d_%s", 42, "test");
    assertEquals("signal_42_test", signal.toString());
}

@Test
void testCharSequenceMethods() {
    SignalType<Integer> signal = SignalType.of("test123");
    assertEquals(7, signal.length());
    assertEquals("st1", signal.subSequence(2, 5).toString());
}
```

### PackageUtilsTest
Tests Java module detection:
```java
@Test
void testIsPackageInstalled() {
    boolean installed = PackageUtils.isPackageInstalled("java.base");
    assertThat(installed).isTrue(); // java.base always present
    
    installed = PackageUtils.isPackageInstalled("non.existent.module");
    assertThat(installed).isFalse();
}
```

### SslUtilsTest
Tests SSL context creation:
```java
@Test
void testClientContextWithVerification() {
    SSLContext context = SslUtils.clientContext(
        SslUtils.SslProfile.PYTHON_DEFAULT, true);
    
    assertThat(context).isNotNull();
    assertThat(context.getProtocol()).isEqualTo("TLS");
}
```

### UlidUtilsTest
Tests with disabled state for pending dependencies:
```java
@Test
@Disabled("ULID dependency not yet added")
void testNewUlid() {
    assertThrows(UnsupportedOperationException.class, UlidUtils::newUlid);
}
```

## Test Patterns and Best Practices

### 1. Descriptive Test Names
Use clear, intention-revealing names:
```java
void testValidPercentage() // Good
void test1() // Bad
```

### 2. Arrange-Act-Assert Pattern
```java
@Test
void testDeviceNotFoundException() {
    // Arrange
    String deviceId = "sensor.bedroom";
    
    // Act
    NexsysException exception = NexsysException.deviceNotFound(deviceId);
    
    // Assert
    assertEquals(ErrorCategory.DEVICE, exception.getCategory());
}
```

### 3. Parameterized Tests
Use JUnit 5's parameterized tests for multiple inputs:
```java
@ParameterizedTest
@ValueSource(ints = {0, 1, 50, 99, 100})
void testValidRange(int value) {
    assertDoesNotThrow(() -> Percentage.of(value));
}
```

### 4. Nested Test Classes
Organize related tests using nested classes:
```java
@Nested
@DisplayName("Edge Cases")
class EdgeCases {
    @Test
    void testEmptyList() { ... }
    
    @Test
    void testNullInput() { ... }
}
```

### 5. AssertJ Fluent Assertions
Prefer AssertJ for readable assertions:
```java
// AssertJ style (preferred)
assertThat(installed).contains("java.base", "java.logging");
assertThat(installed).doesNotContain("fake.module");

// Standard JUnit style
assertTrue(installed.contains("java.base"));
assertFalse(installed.contains("fake.module"));
```

### 6. Test Data Builders
For complex objects, use builder pattern:
```java
NexsysException exception = NexsysException.builder()
    .message("Test error")
    .category(ErrorCategory.DEVICE)
    .severity(ErrorSeverity.WARNING)
    .errorCode("TEST_001")
    .build();
```

### 7. Edge Case Testing
Always test:
- Null inputs
- Empty collections
- Boundary values
- Invalid states
- Concurrent access (where applicable)

## Test Coverage Goals

- **Line Coverage**: Target 80%+ for utility classes
- **Branch Coverage**: Target 75%+ for complex logic
- **Critical Path Coverage**: 100% for security and exception handling

## Continuous Integration

Tests are automatically run on:
- Every commit push
- Pull request creation
- Nightly builds

## Debugging Tests

### Enable Detailed Logging
```bash
./gradlew test --debug
```

### Run Single Test Method
```bash
./gradlew test --tests "PercentageTest.testValidPercentage"
```

### Generate Test Reports
```bash
./gradlew test
# Reports available at: build/reports/tests/test/index.html
```

## Common Test Issues and Solutions

### 1. Flaky Tests
- Use `@RepeatedTest(10)` to identify intermittent failures
- Avoid time-dependent assertions
- Mock external dependencies

### 2. Slow Tests
- Use `@Tag("slow")` to categorize slow tests
- Run fast tests separately in CI
- Consider using `@Timeout` annotations

### 3. Test Isolation
- Use `@BeforeEach` to reset state
- Avoid static mutable state
- Clear MDC context in exception tests

## Future Test Enhancements

1. **Integration Tests**: Add integration test suite for component interactions
2. **Performance Tests**: Add JMH benchmarks for critical paths
3. **Mutation Testing**: Integrate PIT for test quality verification
4. **Contract Tests**: Add tests for external API contracts
5. **Property-Based Tests**: Consider jqwik for property-based testing

## Contributing Tests

When adding new functionality:
1. Write tests first (TDD approach)
2. Ensure all edge cases are covered
3. Follow existing naming conventions
4. Add appropriate assertions
5. Document complex test scenarios
6. Ensure tests are deterministic and fast

## Test Maintenance

- Review and update tests when refactoring
- Remove obsolete tests
- Keep test data minimal and focused
- Refactor common test utilities to base classes
- Monitor test execution time trends