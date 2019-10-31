# Galaxy Middleware API

This API is implemented in order to integrate the MIP front-end with Galaxy through a RestAPI.

## Requirements

- ### Build Project

    - [Java8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
    - [Gradle](https://gradle.org/)

- ### Deploy Project

    - Application Server(i.e. [Tomcat](http://tomcat.apache.org/))

## Building the docker image:

1. In the project directory :
    ```sh
    gradle clean
    ```
2. In the project directory :
    ```sh
    gradle war
    ```
3. docker build -t hbpmip/galaxy_middleware_api .

## Dependencies:

1. Galaxy has to be installed (https://github.com/madgik/galaxy/tree/master/Docker_Build_Scripts).
2. After installing Galaxy an API key should be created:
    - Enter Galaxy from the browser.
    - Select the "User" Drop Down menu, on the navigation bar.
    - Select the "Preferences" option.
    - Select the "Manage API Key" option.
    - "Create a new key"
    - Copy the key so we can use it in the next step.

## Deploy:

Use the following command after changing the appropriate variables:
  - "galaxyUrl" should be replaces by the url of the server that has Galaxy installed.
  - "galaxyApiKey" should be replaced by the API key created in Galaxy.
  - "galaxyReverseProxyPassword" has to be the same as the password given when installing Galaxy.
  - "jwtSecret" has to be the same as in the HBPMedical/portal-backend.
  - Change the tag name of the docker "galaxy_middleware_api" image to the version you want to install.

```sh
docker run -d -e jwtSecret='1234567890' -e jwtIssuer='Online JWT Builder' -e galaxyURL='http://88.197.53.123/' -e galaxyApiKey='1234541541351' -e galaxyReverseProxyUsername='admin' -e galaxyReverseProxyPassword='password' -p 8091:8080 hbpmip/galaxy_middleware_api:v0.3.1
```

If you want, you can save all the environmental variables and use the following instructions to deploy:

1. You can follow this template to create the env.list file :
    ```
    jwtSecret=theJwtSecret
    jwtIssuer=theJwtIssuer
    galaxyURL=YourGalaxyURL
    galaxyApiKey=YourGalaxyApiKey
    galaxyReverseProxyUsername=admin
    galaxyReverseProxyPassword=password
    ```
2. Run the docker image:
    ```sh
    docker run --env-file env.list -p 80:8080 hbpmip/galaxy_middleware_api:v0.3.1
    ```


You can use the api from the following endpoint
```http://localhost:PORT/Galaxy_Middleware_API-1.0.0-SNAPSHOT/api/getWorkflows```


## Test:

Generate a JWT token with HMAC512 security. Then update jwtToken variable in [Postman](https://www.getpostman.com/) and test the API calls.
