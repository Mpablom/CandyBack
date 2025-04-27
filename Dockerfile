# Usa una imagen base de Java 17
FROM eclipse-temurin:17-jdk

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el proyecto al contenedor (incluyendo pom.xml y código fuente)
COPY . .

# Empaqueta la aplicación con Maven
RUN mvn clean package

# Expone el puerto que usa tu aplicación (ej: 8080)
EXPOSE 8080

# Comando para ejecutar el JAR generado
CMD ["java", "-jar", "target/candybar-0.0.1-SNAPSHOT.jar"]
