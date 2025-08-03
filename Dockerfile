# Stage 1: Build the application using Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy all project files
COPY . .

# Package the application (skip tests for faster build if desired)
RUN mvn clean package -DskipTests

# Stage 2: Run the application with minimal JDK image
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /oauthApp

# Copy the built JAR file from Stage 1
COPY --from=build /app/target/*.jar oauthApp.jar

# Expose the port your Spring Boot app uses
EXPOSE 8081

# Run the application
ENTRYPOINT ["java", "-jar", "oauthApp.jar"]
