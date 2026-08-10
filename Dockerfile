# Etapa 1: build - compila el proyecto con Maven
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: runtime - imagen final, ligera, sin Maven
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

RUN mkdir -p /app/data/listas-asistencia

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]