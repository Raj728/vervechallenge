FROM maven:3.8.8-eclipse-temurin-17 AS build
WORKDIR /app


COPY pom.xml .
COPY src ./src


RUN mvn clean package -DskipTests


FROM openjdk:17-jdk-slim
WORKDIR /app

COPY --from=build /app/target/vervegroupchallenge.war /app/app.war

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.war"]
