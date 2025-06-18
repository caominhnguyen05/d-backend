# Stage 1: Build the application using Maven
FROM eclipse-temurin:21-jdk-jammy AS builder
# Using eclipse-temurin as it's a good OpenJDK distribution.
# Make sure the Java version (21 here) matches your project.

WORKDIR /app

# Copy the Maven wrapper files
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Copy the rest of the application source code
COPY src ./src

# Make mvnw executable
RUN chmod +x ./mvnw

# Build the application and create the executable JAR
RUN ./mvnw clean package -DskipTests

# Stage 2: Create the final lightweight image
FROM eclipse-temurin:21-jre-jammy
# Using a JRE image for a smaller final image

WORKDIR /app

# Copy the JAR from the builder stage
# The JAR was built in /app/target/ in the 'builder' stage
COPY --from=builder /app/target/*.jar app.jar

# Expose port (ensure your app listens on PORT env var or this port)
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]