FROM openjdk:17-jdk-slim

RUN apt-get update -qq && apt-get install -y maven

COPY pom.xml target/
WORKDIR target
RUN mvn dependency:copy-dependencies -DoutputDirectory=lib

COPY . /app

WORKDIR /app

CMD ["mvn", "spring-boot:run"]
#HEALTHCHECK --interval=10s --timeout=30s --start-period=10s --retries=3 CMD curl -f http://localhost:8082/actuator/health || exit 1
