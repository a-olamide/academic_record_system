# ===== Build stage =====
FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app

# Cache dependencies first
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

# Now copy sources and build
COPY src ./src
RUN mvn -q -DskipTests package

# ===== Runtime stage =====
FROM eclipse-temurin:21-jre
WORKDIR /opt/app

# (Optional) run as non-root
RUN useradd -r -s /usr/sbin/nologin appuser

# Copy the fat jar from the build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
USER appuser
ENTRYPOINT ["java","-jar","/opt/app/app.jar"]