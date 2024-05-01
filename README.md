# PantryPal

Fall 2023 CSE110 Group 13.
Github link: https://github.com/ucsd-cse110-fa23/cse-110-project-team-13.git

## Description

PantryPal uses audio input for meal types and a list of available ingredients, the app employs the ChatGPT API to generate bespoke recipes. Each recipe is complemented with a unique image, meticulously rendered by the Dall-E API. enhancing the visual appeal. PantryPal allows the user to modify these AI generated recipe to fit their needs, and share URLs of the generaed recipes with friends! 

## Demo
https://img.youtube.com/vi/t3-_FpZTYsY/maxresdefault.jpg

## Getting Started + Installation

Make sure you have Java 21 set up on your system since this project uses a toolchain for Java 21. 

Begin by forking and cloning the repository from https://github.com/ucsd-cse110-fa23/cse-110-project-team-13.git.

You can install the dependencies using Gradle:

```
./gradlew build
```

## Executing the program

To run the application, first make a clean build using the following command.

```
./gradlew clean build
```

Then, run using:

```
./gradlew run
```

### Dependencies

- **JavaFX (18.0.1)**: Used for creating user interfaces.
- **MongoDB Driver (4.11.0)**: Integrates MongoDB for database operations.
- **Java EE API (8.0.1)**: Provides the Java Enterprise Edition API.
- **Javalin (5.6.3)**: A lightweight web framework for Kotlin and Java.
- **Jackson Databind (2.16.0)**: Used for JSON serialization and deserialization.
- **SLF4J (2.0.9)**: Simple logging framework.
- **Gson (2.10.1)**: Google's JSON library for Java.
- **Bcrypt (0.10.2)**: Library for hashing passwords.
- **JSON (20231013)**: For parsing and formatting JSON data.

### Test Dependencies

- **JUnit Jupiter (5.10.1)**: Provides the JUnit 5 API and TestEngine.
- **Mockito (3.12.4) & PowerMock (2.0.9)**: For mocking in tests.
- **OpenJFX Monocle (jdk-12.0.1+2)**: For testing JavaFX applications.

## Version History

Please look at the [commit history](https://github.com/ucsd-cse110-fa23/cse-110-project-team-13/commits/main).


## Authors 

Delaware Wade, Hao Chen, Matthew Hsu, Nicole Hernandez, Baljot Sekhon, Charvi Shukla.






