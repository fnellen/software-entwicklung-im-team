FROM openjdk:17-jdk-alpine
COPY / /
RUN ./gradlew build
ARG JAR_FILE=chicken_spring/build/libs/chicken_spring-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
