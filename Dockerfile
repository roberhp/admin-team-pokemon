# ===============================
# Stage 1 - Build
# ===============================
FROM maven:3.9.11-eclipse-temurin-17 AS builder

WORKDIR /app

# Copiamos primero el pom.xml para aprovechar la caché de Docker
COPY pom.xml .

# Descarga las dependencias
RUN mvn dependency:go-offline

# Ahora copiamos el resto del proyecto
COPY . .

# Compilamos la aplicación
RUN chmod +x mvnw

RUN ./mvnw clean package -DskipTests

# ===============================
# Stage 2 - Runtime
# ===============================
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copiamos únicamente el JAR generado en la etapa anterior
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]