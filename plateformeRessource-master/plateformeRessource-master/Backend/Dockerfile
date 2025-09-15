# Étape de build
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Étape d'exécution
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/ressourceRelationnelle-0.0.1-SNAPSHOT.jar .
ENTRYPOINT ["java", "-jar", "ressourceRelationnelle-0.0.1-SNAPSHOT.jar"]
