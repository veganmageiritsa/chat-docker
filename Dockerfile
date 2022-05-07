#our base image
FROM adoptopenjdk/openjdk11-openj9:jdk-11.0.14.1_1_openj9-0.30.1-alpine

# Copy app
VOLUME /main-app
ADD target/chat-docker-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]
