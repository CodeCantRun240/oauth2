# Use a minimal Java image
FROM eclipse-temurin:21-jdk-jammy

# Set working directory
WORKDIR /oauthApp

# Copy the jar file into the image
COPY target/*.jar oauthApp.jar

# Expose the port (optional)
EXPOSE 8081

# Command to run the app
ENTRYPOINT ["java", "-jar", "oauthApp.jar"]
