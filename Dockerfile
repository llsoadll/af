# Use Java 17 base image
FROM openjdk:17-jdk-slim
# Set working directory
WORKDIR /app
# Copy Maven wrapper and pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
# Give execution permissions
RUN chmod +x ./mvnw
# Copy source code
COPY src ./src
# Copy .env file
COPY .env .
# Build the application
RUN ./mvnw clean package -DskipTests
# Expose port (adjust if needed)
EXPOSE 8080
# Run the jar file with environment variables
ENTRYPOINT ["sh", "-c", "java -jar target/gestion-estudiante-0.0.1-SNAPSHOT.jar"]