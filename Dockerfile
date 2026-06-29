FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY pom.xml .
COPY employee-app/pom.xml employee-app/pom.xml
COPY employee-app/src employee-app/src

RUN mvn -f pom.xml -pl employee-app -am package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /workspace/employee-app/target/employee-app-*.jar app.jar

RUN mkdir -p /app/data
VOLUME ["/app/data"]

ENV APP_DATA_DIR=/app/data
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
