# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

COPY app-order/pom.xml .
RUN mvn dependency:go-offline -B

COPY app-order/src ./src
RUN mvn clean package -DskipTests


# Stage 2: Download New Relic Agent (Dedicated Stage)
FROM alpine:latest AS newrelic-agent
RUN apk add --no-cache curl unzip
WORKDIR /newrelic
RUN curl -L -O https://download.newrelic.com/newrelic/java-agent/newrelic-agent/current/newrelic-java.zip && unzip newrelic-java.zip && rm newrelic-java.zip

# Stage 3: Create the runtime image 

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
COPY --from=newrelic-agent /newrelic/newrelic/newrelic.jar /app/newrelic/newrelic.jar
EXPOSE 8080
CMD ["java", "-javaagent:/app/newrelic/newrelic.jar", "-jar", "app.jar"]