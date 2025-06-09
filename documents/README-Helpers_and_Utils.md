# NexSys Helper & Utility Library

> **Scope:** `src/main/java/com/nexsys/util/**`
> **Audience:** NexSys core developers, integration authors, and advanced users extending or embedding the platform.

---

## 📚 Table of Contents

1. [Overview](#overview)
2. [Design Principles](#design-principles)
3. [Module Map](#module-map)
4. [Detailed API Notes](#detailed-api-notes)

    1. [Collections & Concurrency](#collections--concurrency)
    2. [Color Processing](#color-processing)
    3. [Date & Time](#date--time)
    4. [File I/O & Storage](#file-io--storage)
    5. [HTTP & Networking](#http--networking)
    6. [Images & Graphics](#images--graphics)
    7. [Language & Internationalisation](#language--internationalisation)
    8. [Logging & Diagnostics](#logging--diagnostics)
    9. [Math, Scaling & Statistics](#math-scaling--statistics)
    10. [System, Threads & Processes](#system-threads--processes)
    11. [Unit Conversion Framework](#unit-conversion-framework)
5. [Cross‑Cutting Utilities](#cross-cutting-utilities)
6. [Testing Strategy](#testing-strategy)
7. [Planned Dependencies & TODOs](#planned-dependencies--todos)
8. [Versioning & Compatibility](#versioning--compatibility)
9. [Contributing](#contributing)

---

## Overview

The **Helper & Utility Library** forms the bedrock of NexSys. It provides small, **framework‑agnostic** building blocks that:

* Keep third‑party dependencies to a minimum (core util package is vanilla JDK + SLF4J/Logback).
* Embrace modern **Java 21+ idioms** (records, sealed interfaces, virtual threads).
* Mirror battle‑tested Python helpers from Home Assistant while staying idiomatic to the JVM.
* Are **100% unit‑tested** and designed for reuse across the platform.

> **Tip:** The helpers are intentionally lightweight. Whenever you need higher‑level features (e.g. YAML schema validation, sunrise/sunset calculations), look at `TODO` comments—dependencies may be introduced once they graduate from the *wish list*.

---

## Design Principles

| Principle                        | Rationale                                                                                                   |
| -------------------------------- | ----------------------------------------------------------------------------------------------------------- |
| **Fail‑fast, fail‑loud**         | Most methods throw `IllegalArgumentException`/`NullPointerException` instead of returning `null`.           |
| **Immutable by default**         | Records, unmodifiable collections, and value objects prevent accidental mutation.                           |
| **Virtual thread‑friendly**      | Blocking operations are protected via [`LoopGuard`](#loopguard) and executed on `NexsysExecutor`.           |
| **Prefix ‑Utils classes**        | Clear, stateless functions grouped by domain; all `private` constructors.                                   |
| **Keep I/O at edges**            | I/O heavy helpers (e.g. `FileUtils`, `ImageUtils`) isolate platform specifics.                              |
| **Minimal logging inside utils** | Except for error paths or explicit helpers (`LoggingUtils`), we avoid chatty logs inside low‑level helpers. |
| **100 % test discipline**        | Every public method ships with a unit‑test. We mirror HA test cases for parity where sensible.              |

---

## Module Map

```
com.nexsys.util
├── CollectionUtils.java      // list slicing, chunking, deep‑merge
├── ColorUtils.java           // CSS names, RGB↔XY, HSV/HSL, Kelvin, gamut math
├── DateTimeUtils.java        // ISO‑8601 parse/format, epoch, now*, diff
├── EventType.java            // typed string wrapper for event bus
├── FileUtils.java            // UTF‑8 atomic writes with POSIX perms
├── HttpClientUtils.java      // minimal async GET/POST powered by virtual‑thread executor
├── ImageUtils.java           // read/write, resize, crop, rotate, draw bounding boxes
├── JsonUtils.java            // thin Jackson façade
├── LanguageUtils.java        // dialect matching, region hints, wildcards
├── LimitedSizeMap.java       // FIFO bounded LRU-ish map (no access order)
├── LoggingUtils.java         // turn on async appenders globally
├── LoopGuard.java            // protect event‑loop threads from blocking ops
├── NetworkUtils.java         // IP/URL helpers, CIDR checks, URL normalisation
├── NexsysException.java      // rich, MDC‑aware exception hierarchy
├── NexsysExecutor.java       // singleton scheduler + virtual‑thread pool
├── NormalizedNameRegistry.java // case‑/whitespace‑insensitive registries
├── PackageUtils.java         // module presence checks (Jigsaw friendly)
├── Percentage[Helper].java   // human‑friendly % helpers
├── ProcessUtils.java         // kill / dummy process helpers
├── ReadOnlyMap.java          // safe read‑only views & copies
├── ScalingUtils.java         // range scaling maths
├── SignalType.java           // typed string wrapper for signals
├── SslUtils.java             // quick SSLContext presets
├── StatisticsUtils.java      // variance suppression, memoization
├── SystemInfo.java           // CPU, memory, Docker/K8s heuristics
├── TemperatureUtils.java     // conversions & display helpers
├── ThreadUtils.java          // safe shutdown, polite interrupt
├── TimeoutUtils.java         // CompletableFuture timeouts
├── UlidUtils.java            // ULID stubs (dependency TODO)
├── UnitConverter.java        // core conversion engine
├── UnitSystem.java           // Metric vs US Customary presets
├── UuidUtils.java            // hyphen‑less UUID generator
└── YamlUtils.java            // SnakeYAML Engine façade
```

<details>
<summary>Units namespace</summary>

```
com.nexsys.util.units
├── Unit.java                 // sealed, parent for all unit enums
├── LengthUnit.java           // millimetre, foot, mile …
├── AreaUnit.java             // square‑metre, acre …
├── MassUnit.java             // gram, pound …
├── VolumeUnit.java           // litre, gallon …
├── PressureUnit.java         // pascal, psi …
├── TemperatureUnit.java      // °C, °F, K
├── SpeedUnit.java            // m/s, km/h, knots, Beaufort …
├── EnergyUnit.java           // Wh, kWh …
├── PowerUnit.java            // W, kW …
├── TimeUnit.java             // second, hour …
├── ... (and 11 more) ...
└── Quantity.java             // value + unit with pretty `toString()`
```

</details>

---

## Detailed API Notes

### Collections & Concurrency

#### `CollectionUtils`

* `take(iterable, n)` – cheap slice that works on any `Iterable`.
* `chunk(list, size)` – returns *copy* sub‑lists; no aliasing.
* `chunkedOrAll(collection, size)` – convenience for APIs that accept single or chunked payloads.
* `deepMerge(target, source, conflictResolver)` – recursive merge for nested maps; conflict resolver lambda chooses winner.

```java
Map<String,Object> merged = CollectionUtils.deepMerge(a, b, (left,right) -> right);
```

#### `LimitedSizeMap`

Lightweight bounded map for caches/queues where **insertion order** is what matters (FIFO).  Uses `LinkedHashMap.removeEldestEntry(...)`.

#### `NexsysExecutor`

* Virtual threads (`Executors.newVirtualThreadPerTaskExecutor()`) when available.
* Separate **scheduler** for delayed tasks with `runAt` / `runLater`.
* `submitBlocking()` wraps blocking I/O inside virtual thread, returning a `CompletableFuture<T>`.

### Color Processing

`ColorUtils` is a near‑feature‑parity port of Home Assistant’s color math:

| Feature                  | Method(s)                           | Notes                                       |
| ------------------------ | ----------------------------------- | ------------------------------------------- |
| CSS name lookup          | `nameToColor(String)`               | 148 named colors inc. HA blue.              |
| RGB ↔ XY (CIE 1931)      | `rgbToXy(Color, Gamut)` / `xyToRgb` | Optional gamut clipping.                    |
| RGB ↔ HSV                | `rgbToHsv`, `hsvToRgb`              | Returns HSV array, uses `double` precision. |
| Temperature (K) → RGB    | `temperatureToRgb(k)`               | 1000 K – 40000 K.                           |
| Brightness from RGBW     | `rgbwBrightness(Color)`             | Simplistic max‑channel approach.            |
| Gamut validation helpers | in `color.Gamut` record             | `isValid()` ensures triangle not collinear. |

All color structs are immutable `record`s (`Color`, `XYPoint`, `Gamut`).

### Date & Time

`DateTimeUtils` wraps the Java Time API:

* `nowUtc()`, `now()`, `now(zone)` – convenience for clock injection.
* Robust ISO‑8601 parsing via `Instant.parse` fall‑backs.
* `between(start,end)` – delegate to `Duration`.
* TODO: sunrise/sunset once **Commons SunCalc** is added.

### File I/O & Storage

| Class       | Highlights                                                                                |
| ----------- | ----------------------------------------------------------------------------------------- |
| `FileUtils` | *Atomic* UTF‑8 writes (`writeUtf8Atomic`), POSIX permission toggles `600/644`, safe read. |
| `YamlUtils` | SnakeYAML Engine wrapper; `loadDict` enforces top‑level map.                              |
| `JsonUtils` | Thin Jackson; deliberately returns `Object`/`JsonNode` to avoid binding decisions.        |

### HTTP & Networking

* **HttpClientUtils** – async GET/POST JSON with per‑call timeout; under the hood uses `NexsysExecutor.submitBlocking` so that body sends do not pin platform threads.
* **NetworkUtils** – IP validators, private/loopback heuristics, CIDR containment, smart `normalizeUrl()` to strip default ports & trailing slashes.

### Images & Graphics

`ImageUtils` utilises AWT:

* Read/write (`BufferedImage`) from paths/streams/base64.
* Basic transforms (resize w/ aspect‑ratio, crop, rotate).
* `drawBoundingBox(...)` replicates HA’s object‑detection overlay (normalized coords, label).

### Language & Internationalisation

`LanguageUtils` + `language.Dialect` provide fuzzy matching suitable for speech/NLU engines:

* Aliases (`nb`↔`no`, `iw`↔`he`).
* Region vs Script heuristics (`zh‑Hant`, `es‑419`).
* Quality scoring for **Accept‑Language** headers or UI negotiation.

### Logging & Diagnostics

* `LoggingUtils.activateAsyncLogging()` – flips all Logback `AsyncAppender`s to lower latency queue sizes; call early on bootstrap.
* `NexsysException` – structured exceptions with Category/Severity enums and MDC enrichment; includes builder + common factories (configurationError, networkError…).

### Math, Scaling & Statistics

* `ScalingUtils` – map a value between arbitrary ranges (float/int), used by brightness/volume integrations.
* `StatisticsUtils.ignoreVariance(func, threshold)` – memoizing decorator that dampens jitter.
* `PercentageHelper` – human readable % conversions to/from list positions.

### System, Threads & Processes

| Utility        | Purpose                                                                                                  |
| -------------- | -------------------------------------------------------------------------------------------------------- |
| `SystemInfo`   | CPU load, heap usage, Docker/vm detection.                                                               |
| `ThreadUtils`  | Deadlock‑safe JVM shutdown; polite interrupt; access to `Unsafe` (needed for advanced scheduling later). |
| `ProcessUtils` | Force kill + cross‑platform *dummy long‑running* process for tests.                                      |
| `LoopGuard`    | Throws if blocking call executed on marked event‑loop thread; similar to HA’s sync‑in‑async guard.       |
| `TimeoutUtils` | Future/Callable timeout wrappers.                                                                        |

### Unit Conversion Framework

* **Unit** – sealed interface; each physical measure has an `enum` (e.g. `LengthUnit`).
* **UnitConverter** – central engine; also supports quirky conversions (Beaufort scale, EV efficiency `kWh/100 km`).
* **Quantity** – value + unit record with smart `toString()` symbols.
* **UnitSystem** – *metric* vs *us\_customary* presets & device‑class conversions (e.g. gas meter ft³→m³).

---

## Cross‑Cutting Utilities

| Wrapper Type               | Description                                                   |
| -------------------------- | ------------------------------------------------------------- |
| `EventType<D extends Map>` | Type‑safe event identifiers (`CharSequence`).                 |
| `SignalType<P>`            | Same idea for *signals* flowing through the automation graph. |
| `ReadOnlyMap`              | Null‑safe, immutable views/copies.                            |
| `PackageUtils`             | Jigsaw aware *is‑module‑installed?* queries.                  |
| `UlidUtils`                | Placeholder until `de.huxhorn.sulky:ulid` is added.           |
| `UuidUtils`                | 32‑char UUID hexs for database keys.                          |

---

## Testing Strategy

All helpers are covered by **JUnit 5** tests under `src/test/java`. We adopt:

* **AssertJ** fluent assertions.
* **Virtual timeouts** when tests spawn threads/processes.
* Homage parity—tests mimic HA’s Python cases when behaviour must match 1:1.

> *Coverage target:* **100 % lines** for util package. See `README-Tests.md` for generated coverage badges per module.

---

## Planned Dependencies & TODOs

| Utility / Feature       | Dependency                                      | Gradle Notation                                              |
| ----------------------- | ----------------------------------------------- | ------------------------------------------------------------ |
| `CollectionUtils.chunk` | Guava `Lists.partition` (optional optimisation) | `implementation "com.google.guava:guava:32.1.3-jre"`         |
| Sunrise/Sunset          | Commons SunCalc                                 | `implementation "org.shredzone.commons:commons-suncalc:3.7"` |
| SSL Cipher Suites       | BouncyCastle Provider                           | `implementation "org.bouncycastle:bcprov-jdk18on:<ver>"`     |
| ULIDs                   | Sulky ULID                                      | `implementation "de.huxhorn.sulky:ulid:8.2.0"`               |
| JSR‑385 Units           | Indriya (tech.units)                            | `implementation "tech.units:indriya:3.2"`                    |

Dependencies are **scoped** and only added once an integration truly requires them.

---

## Versioning & Compatibility

* The util API adheres to **Semantic Versioning** but breaking changes may occur until **v1.0**.
* Once NexSys hits beta, helper APIs will be frozen except for additive evolution.

---

## Contributing

1. Follow the general project [CONTRIBUTING](../../CONTRIBUTING.md) guide.
2. For new helpers, include:

    * Thorough Javadoc with examples.
    * Unit tests hitting edge‑cases.
    * No additional dependencies unless approved in issue tracker.
3. Run `./gradlew spotlessApply test` before opening PR.

> *Thank you for helping make NexSys robust and developer‑friendly!*
