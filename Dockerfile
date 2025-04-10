# Build stage: Use Maven to compile and package the application
FROM maven:3.8.6-openjdk-17-slim AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven configuration files first
# This is a performance optimization that allows Docker to cache dependencies
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
COPY mvnw.cmd .

# Make the Maven wrapper executable (addresses the permission issue)
RUN chmod +x mvnw

# Download all dependencies (will be cached if pom.xml doesn't change)
RUN ./mvnw dependency:go-offline -B

# Copy the project source code
COPY src src

# Package the application, skipping tests to speed up the build
RUN ./mvnw package -DskipTests

# Runtime stage: Use a smaller base image for the final container
FROM eclipse-temurin:17-jre-alpine

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port the app runs on
EXPOSE 8080

# Command to run the application with environment variables
ENTRYPOINT ["java", "-jar", "app.jar"]