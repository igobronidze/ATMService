FROM maven:3.8-openjdk-8 AS builder
COPY pom.xml /src/atm-service/
WORKDIR /src/atm-service
RUN mvn dependency:resolve
COPY . /src/atm-service/
RUN mvn clean install

FROM openjdk:8-jdk
COPY --from=builder /src/atm-service/target/atmservice-0.0.1-SNAPSHOT.jar /app/atm-service/
WORKDIR /app/atm-service/
CMD java -jar atmservice-0.0.1-SNAPSHOT.jar
