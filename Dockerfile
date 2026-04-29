FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests
RUN find /app/target -name "*.jar" -not -name "*sources*"

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/target/job-portal-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java"]
CMD ["-jar", "/app/app.jar"]
