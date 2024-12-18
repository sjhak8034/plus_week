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

ENV DATABASE_URL = jdbc:mysql://localhost:3306/plus_week
ENV DATABASE_USERNAME = root
ENV DATABASE_PASSWORD = 1234

ENV DATABASE_URL = org.h2.Driver
ENV DATABASE_USERNAME = sa
ENV DATABASE_PASSWORD =  ""



# Set default command to run the application
CMD ["java", "-jar", "build/libs/your-app.jar"]
