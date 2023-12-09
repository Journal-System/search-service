FROM maven:3.8.1-openjdk-17-slim AS build
COPY src /app/src
COPY pom.xml /app
RUN mvn -f app/pom.xml install

FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY --from=build /app/target/searchservice-1.0.0-SNAPSHOT.jar /app/searchservice-1.0.0-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/searchservice-1.0.0-SNAPSHOT.jar"]

