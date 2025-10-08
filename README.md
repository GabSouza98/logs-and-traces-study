# ELK Test
Run with "docker-compose up -d"

Every serviceA, serviceB and serviceC generates logs.

ServiceA exposes a GET method on "http://localhost:8080/v1/start-trace", which then calls serviceB, which in turn calls serviceC. Each method uses Thread.sleep() to simulate a long-running API call.
Each service contains a logback-spring.xml which writes logs in 2 ways:

1) Send logs directly to logstash via TCP
```
    <appender name="LOGSTASH" class="net.logstash.logback.appender.LogstashTcpSocketAppender">
        <destination>logstash:5044</destination> <!-- Send logs to Logstash -->
        <encoder class="net.logstash.logback.encoder.LogstashEncoder"/>
    </appender>
```

2) Writes logs to a file, and then Filebeat sends the logs to logstash
```
    <springProperty name="SERVICE_NAME" source="spring.application.name"/>
    <property name="LOG_FILE" value="/logs/${SERVICE_NAME}.json"/>
    <include resource="org/springframework/boot/logging/logback/defaults.xml"/>

    <appender name="ECS_FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <encoder class="co.elastic.logging.logback.EcsEncoder">
            <serviceName>${SERVICE_NAME:-spring-boot-application}</serviceName>
        </encoder>
        <file>${LOG_FILE}</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <cleanHistoryOnStart>${LOG_FILE_CLEAN_HISTORY_ON_START:-false}</cleanHistoryOnStart>
            <fileNamePattern>${ROLLING_FILE_NAME_PATTERN:-${LOG_FILE}.json.%d{yyyy-MM-dd}.%i.gz}</fileNamePattern>
            <maxFileSize>${LOG_FILE_MAX_SIZE:-20MB}</maxFileSize>
            <maxHistory>${LOG_FILE_MAX_HISTORY:-10}</maxHistory>
            <totalSizeCap>${LOG_FILE_TOTAL_SIZE_CAP:-0}</totalSizeCap>
        </rollingPolicy>
    </appender>
```
   
There's a volume created on path "/logs". Each service writes logs in the following path "/logs/serviceX.json"

The root container will have a folder structure like this:
/logs
  serviceA.json
  serviceB.json
  serviceC.json

Then Filebeat will watch the /logs folder to ingest the logs to logstash.

```
filebeat.inputs:
  - type: log
    paths:
      - /logs/*.json
    json.keys_under_root: true
    json.add_error_key: true
    json.message_key: message

output.logstash:
  hosts: ["logstash:5044"]
```

Logstash will only forward the logs to elasticsearch. The input may be tcp (for option 1) or beats (for option 2):
```
input {
#  tcp {
#    port => 5044
#    codec => json
#  }
  beats {
    port => 5044
  }
}

output {
  elasticsearch {
    hosts => ["http://elasticsearch:9200"]
    index => "logs-%{+YYYY.MM.dd}"
  }
}

```

### IMPORTANT
Each service (A, B and C) provide a Dockerfile which only copies the generated jar in the workdir. Every service exposes port 8080 internally. 
Everytime you make changes to the services, you must run "mvn install" to regenerate the jar, otherwise the changes won't take effect when running docker-compose up, because the Dockerfile isn't building the project, just copying the previously generated jar.

```
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /serviceA

COPY target/serviceA-0.0.1-SNAPSHOT.jar serviceA.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "serviceA.jar"]
```

Finally, the docker-compose file orchestrates all the services. It also defines an order to starting the containers. 
Note the volume created in each service. There's also a volume created for the logstash.conf file, and for filebeat.yml.
Also, note that since the containers are named "service-a", "service-b" and "service-c", the URL that serviceA uses to call serviceB is actually "http://service-b:8080/v1/trace-service-b", 
and not "http://localhost:8080/v1/trace-service-b", because inside the container, each container sees the other by their names.

```
services:

  service-a:
    build: ./serviceA
    container_name: service-a
    restart: always
    ports:
      - "8080:8080"
    depends_on:
      - service-b
    networks:
      - elk-network
    volumes:
      - ./logs:/logs   # writes logs here

  service-b:
    build: ./serviceB
    container_name: service-b
    restart: always
    ports:
      - "8081:8080"
    depends_on:
      - service-c
    networks:
      - elk-network
    volumes:
      - ./logs:/logs

  service-c:
    build: ./serviceC
    container_name: service-c
    restart: always
    ports:
      - "8082:8080"
    depends_on:
      - filebeat
      - logstash
    networks:
      - elk-network
    volumes:
      - ./logs:/logs

  elasticsearch:
    image: elasticsearch:8.17.1
    container_name: elasticsearch
    restart: always
    volumes:
      - elastic_data:/usr/share/elasticsearch/data/
    environment:
      - xpack.security.enabled=false  # Disable security for local development
      - ES_JAVA_OPTS=-Xmx256m -Xms256m
      - discovery.type=single-node
    ports:
      - '9200:9200'
    networks:
      - elk-network

  logstash:
    image: logstash:8.17.1
    container_name: logstash
    restart: always
    volumes:
      - ./logstash/:/logstash_dir
    command: logstash -f /logstash_dir/pipeline/logstash.conf
    depends_on:
      - elasticsearch
    ports:
      - '5044:5044'
    environment:
      - LS_JAVA_OPTS=-Xmx256m -Xms256m
    networks:
      - elk-network

  kibana:
    image: kibana:8.17.1
    container_name: kibana
    restart: always
    ports:
      - '5601:5601'
    environment:
      - ELASTICSEARCH_URL=http://elasticsearch:9200
    depends_on:
      - elasticsearch
    networks:
      - elk-network

  filebeat:
    image: docker.elastic.co/beats/filebeat:8.17.1
    container_name: filebeat
    user: root
    environment:
      - BEAT_STRICT_PERMS=false
    volumes:
      - /var/lib/docker/containers:/var/lib/docker/containers:ro
      - /var/run/docker.sock:/var/run/docker.sock:ro
      - ./filebeat/filebeat.yml:/usr/share/filebeat/filebeat.yml:ro
      - ./logs:/logs:ro # mount logs, read-only
      - ./data/filebeat:/usr/share/filebeat/data   # persist registry
    depends_on:
      - elasticsearch
      - logstash
    networks:
      - elk-network

networks:
  elk-network:
    driver: bridge

volumes:
  elastic_data: {}
```

This project might be updated to ingest the logs via fluentbit. Also, it might be updated to try different tracing solutions, like OpenTelemetry.


