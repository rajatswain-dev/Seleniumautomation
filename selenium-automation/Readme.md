# 🛒 Amazon Selenium Automation Framework

A Java-based test automation framework for Amazon web application testing, built with Selenium WebDriver, TestNG, and Allure Reporting.

---

## 🧰 Tech Stack

| Tool | Version | Purpose |
|------|---------|---------|
| Java | 17 | Programming language |
| Selenium WebDriver | 4.18.1 | Browser automation |
| TestNG | 7.9.0 | Test framework |
| WebDriverManager | 5.8.0 | Automatic driver management |
| Allure | 2.24.0 | Test reporting |
| AspectJ | 1.9.20 | AOP for Allure step tracking |
| Lombok | 1.18.30 | Boilerplate reduction |
| Maven | 3.x | Build & dependency management |

---

## 📁 Project Structure

```
selenium-automation/
├── src/
│   ├── main/java/com/amazon/
│   │   ├── base/           # BaseTest class (setup/teardown)
│   │   ├── pages/          # Page Object classes
│   │   ├── steps/          # Step definition classes
│   │   ├── listeners/      # TestNG listeners (SuiteListener)
│   │   └── analyzer/       # AnnotationTransformer
│   └── test/
│       ├── java/com/amazon/test/   # Test classes
│       └── resources/
│           └── testng.xml          # TestNG suite configuration
├── allure-results/         # Generated after test run
├── pom.xml
└── README.md
```

---

## ⚙️ Prerequisites

- **Java 17+** installed and `JAVA_HOME` set
- **Maven 3.x** installed
- **Google Chrome** (latest)
- **Allure CLI** installed ([installation guide](https://allurereport.org/docs/install/))

### Install Allure CLI

**Windows (Scoop):**
```bash
scoop install allure
```

**Mac (Homebrew):**
```bash
brew install allure
```

**Linux:**
```bash
sudo apt-get install allure
```

---

## 🚀 Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/your-username/selenium-automation.git
cd selenium-automation
```

### 2. Install dependencies

```bash
mvn clean install -DskipTests
```

### 3. Run the tests

```bash
mvn clean test
```

### 4. Generate & view Allure report

```bash
allure serve target/allure-results
```

---

## 🧪 Test Configuration

Tests are configured via `src/test/resources/testng.xml`.

### Suite Parameters

| Parameter | Default | Description |
|-----------|---------|-------------|
| `browser` | `chrome` | Browser to run tests on |
| `runLocally` | `true` | Run on local machine vs remote grid |
| `implicitWait` | `10` | Implicit wait timeout (seconds) |
| `pageLoadTimeout` | `60` | Page load timeout (seconds) |

### Running a specific test class

```bash
mvn clean test -Dtest=AmazonWebTest
```

### Running with a different browser

Update `testng.xml`:
```xml
<parameter name="browser" value="firefox"/>
```

---

## 📊 Allure Reporting

After running tests, generate the report:

```bash
# Open live report in browser
allure serve target/allure-results

# Or generate static report
allure generate target/allure-results -o allure-report --clean
allure open allure-report
```

The report includes:
- Test execution overview
- Pass/fail/skip statistics
- Step-by-step test breakdown
- Timeline and trend analysis
- Categories and suites view

---

## ✅ Test Cases

| Test ID | Test Name | Description |
|---------|-----------|-------------|
| TC_001 | `verifyCarouselFunctionality` | Verify promotional banner carousel advances on click |

---

## 🏗️ Framework Design

- **Page Object Model (POM)** — UI interactions are encapsulated in page classes
- **Step Layer** — Business-level steps wrapping page actions (used with `@Step` for Allure)
- **BaseTest** — Handles WebDriver initialization and teardown via `@BeforeMethod` / `@AfterMethod`
- **Listeners** — `SuiteListener` for suite-level hooks, `AnnotationTransformer` for dynamic test configuration
- **WebDriverManager** — Automatically downloads and configures the correct ChromeDriver version

---

## ⚠️ Known Warnings

These warnings appear during test runs but do not affect results:

- `SLF4J: Failed to load class StaticLoggerBinder` — Logback configuration warning, harmless
- `Unable to find CDP implementation matching 145` — Chrome version is newer than Selenium's built-in CDP support; update Selenium version to resolve

---

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature`
3. Commit your changes: `git commit -m 'Add some feature'`
4. Push to the branch: `git push origin feature/your-feature`
5. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License.