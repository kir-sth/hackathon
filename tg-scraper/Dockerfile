#FROM gradle:jdk21-jammy AS cache
FROM gradle:jdk17-jammy AS cache
RUN mkdir -p /home/gradle/cache
ENV GRADLE_USER_HOME /home/gradle/cache
WORKDIR /home/gradle/app
COPY tg-scraper-api/build.gradle settings.gradle /home/gradle/app/
RUN gradle init -i --stacktrace

#FROM gradle:jdk21-jammy AS builder
FROM gradle:jdk17-jammy AS builder
COPY --from=cache /home/gradle/cache /home/gradle/.gradle
COPY .. /opt/app
WORKDIR /opt/app/tg-scraper-api
#RUN gradle clean fatJar
RUN gradle clean build

#FROM openjdk:21-jdk-bullseye
FROM openjdk:17.0.2-bullseye
ENV ARTIFACT_NAME=tg-scraper-api-1.0-SNAPSHOT.jar
WORKDIR /opt/app
COPY ../.pg/root.crt .pg/
COPY --from=builder /opt/app/tg-scraper-api/build/libs/$ARTIFACT_NAME ./
ENTRYPOINT exec java -jar ${ARTIFACT_NAME}
