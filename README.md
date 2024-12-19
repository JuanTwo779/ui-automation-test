# OrikanAssessment

This project was created as part of a technical interview for Orikan. It contains automated tests for web applications using Playwright and JUnit. The tests are structured to test various web pages and features of the application.

## Project Structure

- **Artifact Directory**:  
  The artefacts for tasks 1-4 can be found in the `artefacts` directory.

- **Source Code**:
    - **BaseTest Class**: Located in `src/main/java/com/example/app/`. This class serves as the Object Model, which contains shared functionality and setup steps for all tests.
    - **Test Classes**: Located in `src/test/java/com/example/app/`. These classes contain the individual tests for each page or feature of the application.

## Dependencies

The project is built using Maven and has the following dependencies:

- **Playwright**: Version 1.49.0 for web automation and browser testing.
- **JUnit Jupiter**: For running the test cases using JUnit 5.

### Dependencies Section in `pom.xml`:

```xml
<dependencies>
    <dependency>
        <groupId>com.microsoft.playwright</groupId>
        <artifactId>playwright</artifactId>
        <version>1.49.0</version>
    </dependency>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>RELEASE</version>
        <scope>compile</scope>
    </dependency>
</dependencies>
```

## Build Instructions
To build and run the tests, you need to have Maven installed. Once Maven is installed, follow these steps:
1. Clone the repository or download the project.
2. Navigate to the project directory.
3. Use the following Maven command to install the required dependencies:
```text
mvn clean install 
```
4. Run all tests:
```text
In IntelliJ IDEA, right-click on the test folder or package and select Run All Tests.
In Eclipse, you can run all tests by right-clicking on the project and selecting Run As → JUnit Test.
Or, run each test class individually
```