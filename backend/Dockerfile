# Etapa 1: Build
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /build

COPY . .

RUN mvn clean package -DskipTests


# Etapa 2: Runtime
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring

USER spring

COPY --from=build /build/target/app.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
