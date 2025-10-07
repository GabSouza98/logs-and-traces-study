FROM eclipse-temurin:21-jdk-alpine

WORKDIR /serviceB

COPY target/serviceB-0.0.1-SNAPSHOT.jar serviceB.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "serviceB.jar"]
