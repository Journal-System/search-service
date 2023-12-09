FROM maven:3.8.1-openjdk-17-slim AS build
COPY src /app/src
COPY pom.xml /app
RUN mvn -f app/pom.xml install


FROM registry.access.redhat.com/ubi8/openjdk-17:1.17
ENV LANGUAGE='en_US:en'
WORKDIR /app
# We make four distinct layers so if there are application changes the library layers can be re-used
COPY --chown=185 /app/target/quarkus-app/lib/ /deployments/lib/
COPY --chown=185 /app/target/quarkus-app/*.jar /deployments/
COPY --chown=185 /app/target/quarkus-app/app/ /deployments/app/
COPY --chown=185 /app/target/quarkus-app/quarkus/ /deployments/quarkus/
EXPOSE 8085
USER 185
ENV JAVA_OPTS_APPEND="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
ENV JAVA_APP_JAR="/deployments/quarkus-run.jar"
ENTRYPOINT [ "/opt/jboss/container/java/run/run-java.sh" ]