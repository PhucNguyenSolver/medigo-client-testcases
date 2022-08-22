# Medigo App automated test suite
This is a test suite for MedigoApp in dev-environment. You can either run on a mobile emulator locally, or by using AWS device farm.

# Android
## Getting Started

1. Follow the official Appium getting started guide.

2. Open up the **setup** method in BaseClass.java in test folder:
```
@BeforeSuite
public void setup() {
    // setupLocalAppiumDriver(userAppPackage, userAppActivity, userDeviceId);
    // setupAWSDriver();
}
```
3. Either check setupLocalAppiumDriver or setupAWSDriver according to your need.

4. Run the test:
    a. Run locally via cli
    Logout both user account and pharmacy account
    ```
    mvn clean test -Dlocal=true
    ```
    b. Run on AWS device farm
    Build your test package (`zip-with-dependencies.zip` in target folder)
    ```
    mvn clean package -DskipTests=true
    ```
    Upload .app file and test package (in step 1) via the AWS console


---
### Note: 
1. To run the test locally: 
- Your Appium Server and Android Emulator need to be started first.
2. To run the test on AWS device farm: 
- As Device Farm only supports Java 8, do not change the POM properties:
```
    <properties>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
    </properties>
```