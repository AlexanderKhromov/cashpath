# ===================== Stage 1: Build =====================
FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
# if pom.xml was not changed then 'dependency:go-offline' will stay cached and build will go faster
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# ===================== Stage 2: Run =====================
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/cashpath-*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]