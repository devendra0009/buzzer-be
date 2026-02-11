# ========================
# Stage 1: Build the app
# ========================
FROM maven:3.8.8-eclipse-temurin-21 AS build

# Set working directory inside the container
WORKDIR /app

# Copy Maven configuration files first (to leverage caching) -> to /app
COPY pom.xml .

# Download dependencies (cached unless pom.xml changes)
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build the project and skip tests for faster builds
RUN mvn clean package -DskipTests

# ========================
# Stage 2: Run the app
# ========================
FROM eclipse-temurin:21-jdk-alpine

# Set working directory
WORKDIR /app

# Copy the fat JAR from the build stage  -> built the jar under target/jar put it into app/jar
COPY --from=build /app/target/*.jar app.jar

# Expose port (matches Spring Boot default)
EXPOSE 8080

# Pass PORT environment variable if hosting platform requires it
ENV PORT=8080

# Run the Spring Boot app
ENTRYPOINT ["java","-jar","app.jar"]
