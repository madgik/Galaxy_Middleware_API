# Middleware API

This API is implemented in order to integrate front-end with Galaxy through a RestAPI.

## Requirements

- ### Build Project

    - [Java8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
    - [Gradle](https://gradle.org/)

- ### Deploy Project

    - Application Server(i.e. [Tomcat](http://tomcat.apache.org/))

## Build:

1. Checkout project
2. Open command prompt
3. Navigate to project directory
4. Make your own config.properties file :
    ```sh
    vi src/main/resources/config.properties
    ```
5. You can follow this template for config.properties file :
    ```
    # App Properties
    jwtSecret=theJwtSecret
    jwtIssuer=theJwtIssuer
    galaxyURL=YourGalaxyURL
    galaxyApiKey=YourGalaxyApiKey
    ```
6. In the project directory :
    ```sh
    gradle clean
    ```
7. In the project directory :
    ```sh
    gradle war
    ```
8. In the project directory :
    ```sh
    cd build/libs
    ```
9. File .war is in this folder

## Deploy:

#### Via Command Line
1. Install Application Server(i.e. [Tomcat](http://tomcat.apache.org/))
2. Stop Application Server if running(i.e. In [Tomcat](http://tomcat.apache.org/) navigate to root directory and run ```./bin/shutdown.sh ```)
3. Copy .war file in the application folder(i.e. In [Tomcat](http://tomcat.apache.org/) navigate to root directory and copy into webapps directory ```cp path/to/war webapps/ ```)
4. Start Application Server(i.e. In [Tomcat](http://tomcat.apache.org/) navigate to root directory and run ```./bin/startup.sh ```)

#### Via Docker
1. Make configuration using environment variable file :
    ```sh
    vi env.list
    ```
2. You can follow this template for env.list file :
    ```
    jwtSecret=theJwtSecret
    jwtIssuer=theJwtIssuer
    galaxyURL=YourGalaxyURL
    galaxyApiKey=YourGalaxyApiKey
    ```
3. docker pull kkech/middlewareapidocker:latest
4. docker run --env-file env.list -p 80:8080 middlewareapidocker
## Test:

Generate a JWT token with HMAC512 security. Then update jwtToken variable in [Postman](https://www.getpostman.com/) and test the API calls.