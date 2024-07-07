FROM openjdk:17-jdk-slim

RUN apt-get update && apt-get install -y curl

WORKDIR /app

# Ensure the JAR file name is correct
COPY target/trainer-work-service-0.0.1-SNAPSHOT.jar /app/trainer-work-service.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/app/trainer-work-service.jar"]
