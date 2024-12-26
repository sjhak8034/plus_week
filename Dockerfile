# Base image: Use OpenJDK for Java projects
FROM openjdk:17-jdk-slim


ARG DATABASE_ID
ARG DATABASE_PASSWORD
ARG ADMIN_EMAIL

# ⭐ 'ENV' 예약어를 통해 전달받은 값을 실제 값과 매칭시켜야 한다.
ENV DATABASE_ID=${DATABASE_ID} \
    DATABASE_PASSWORD=${DATABASE_PASSWORD} \
    DATABASE_URL=${DATABASE_URL} \


# (2) COPY에서 사용될 경로 변수
ARG JAR_FILE=build/libs/app.jar

# (3) jar 빌드 파일을 도커 컨테이너로 복사
COPY ${JAR_FILE} app.jar

# Set default command to run the application
ENTRYPOINT ["java","-jar","/app.jar"]
