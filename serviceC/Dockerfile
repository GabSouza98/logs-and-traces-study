FROM eclipse-temurin:21-jdk-alpine

WORKDIR /serviceC

COPY target/serviceC-0.0.1-SNAPSHOT.jar serviceC.jar

# Copy OpenTelemetry agent
COPY opentelemetry-javaagent.jar opentelemetry-javaagent.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "serviceC.jar"]
