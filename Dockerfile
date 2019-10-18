FROM tomcat:9.0
LABEL maintainer="kkech"
COPY build/libs/Galaxy_Middleware_API-1.0.0-SNAPSHOT.war /usr/local/tomcat/webapps/Galaxy_Middleware_API-1.0.0-SNAPSHOT.war
