FROM eclipse-temurin:21-jdk-alpine

WORKDIR /serviceA

COPY target/serviceA-0.0.1-SNAPSHOT.jar serviceA.jar

# Copy OpenTelemetry agent
COPY opentelemetry-javaagent.jar opentelemetry-javaagent.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "serviceA.jar"]
