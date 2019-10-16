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

1. Galaxy has to be installed(https://github.com/madgik/galaxy)
2. "galaxyReverseProxyUsername", "galaxyReverseProxyPassword"  has to be the same as Galaxy Reverse proxy(https://github.com/madgik/galaxy/tree/master/Docker_Build_Scripts) username and password.
3. Galaxy user has to produce api key and pass it as parameter "galaxyApiKey"
4. "jwtSecret" has to be the same as HBPMedical/portal-backend.
5. "jwtIssuer" has to be the same as HBPMedical/portal-backend.

## Deploy:

Use the following command after changing the appropriate variables:

```sh
docker run -d -e jwtSecret='1234567890' -e jwtIssuer='Online JWT Builder' -e galaxyURL='http://88.197.53.123/' -e galaxyApiKey='1234541541351' -e galaxyReverseProxyUsername='username' -e galaxyReverseProxyPassword='password' -p 80:8080 hbpmip/galaxy_middleware_api:v1.0.0
```

If you want, you can save all the environmental variables and use the following instructions to deploy:

1. You can follow this template to create the env.list file :
    ```
    jwtSecret=theJwtSecret
    jwtIssuer=theJwtIssuer
    galaxyURL=YourGalaxyURL
    galaxyApiKey=YourGalaxyApiKey
    galaxyReverseProxyUsername=username
    galaxyReverseProxyPassword=password
    ```
2. Run the docker image:
    ```sh
    docker run --env-file env.list -p 80:8080 hbpmip/galaxy_middleware_api:v1.0.0
    ```


## Test:

Generate a JWT token with HMAC512 security. Then update jwtToken variable in [Postman](https://www.getpostman.com/) and test the API calls.