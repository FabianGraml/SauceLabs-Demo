# SauceLabs Demo Project

This repository contains an Appium Java project that uses Sauce Labs for remote testing.

## Getting started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Requirements

- Java
- Maven
- Appium
- Sauce Labs account

### Setup 
1. Clone this repository
2. Intall dependencies: `mvn install`
3. In the `src/test/java` directory, open the `AppiumAndroidEmuAppTest.java` file
4. Replace `yourRemoteURL here` with the remote URL of your SauceLabs account
5. Repeat step 3 and 4 for `AppiumAndroidEmuWebTest.java` file
6. Run the tests using the command `mvn test`

