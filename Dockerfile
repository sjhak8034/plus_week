# Base image: Use OpenJDK for Java projects
FROM openjdk:17-jdk-slim


ARG JAR_FILE=build/libs/app.jar
COPY ${JAR_FILE} app.jar




# Set default command to run the application
ENTRYPOINT ["java","-jar","/app.jar"]
