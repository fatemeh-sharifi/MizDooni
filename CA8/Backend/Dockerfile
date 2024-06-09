FROM openjdk:19
ARG JAR_FILE=target/*.jar
COPY ./target/Mizdooni-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "/app.jar" ]