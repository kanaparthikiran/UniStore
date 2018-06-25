FROM tomcat:8.5-alpine
VOLUME /tmp
ARG JAR_FILE
COPY target/UniStore-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/UniStore.war
RUN sh -c touch  /usr/local/tomcat/webapps/UniStore.war
ENTRYPOINT ["sh","-c", "-Djava.security.egd=file:/dev/./urandom","-jar","/usr/local/tomcat/webapps/UniStore.war"]


# FROM openjdk:8-jdk-alpine
# VOLUME /tmp
# ARG JAR_FILE
# COPY ${JAR_FILE} UniStore-0.0.1-SNAPSHOT.war
# ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/UniStore-0.0.1-SNAPSHOT.war"]