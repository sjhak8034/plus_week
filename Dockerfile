# Base image: Use OpenJDK for Java projects
FROM openjdk:17-jdk-slim

# Set environment variables
ENV APP_HOME=/app
WORKDIR $APP_HOME

# Copy Gradle wrapper and other necessary files
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle build.gradle
COPY src src

# Make Gradle wrapper executable
RUN chmod +x gradlew

# Build the application
RUN ./gradlew build

# Expose the application port (adjust according to your app)
EXPOSE 8080

# Set default command to run the application
CMD ["java", "-jar", "build/libs/your-app.jar"]
