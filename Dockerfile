# Etapa 1: Build (Compilação)
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Compila o projeto e gera o .jar, pulando os testes para ser mais rápido no build inicial
RUN mvn clean package -DskipTests

# Etapa 2: Runtime (Execução)
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
# Copia o .jar gerado na etapa anterior para a imagem final
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta padrão do Spring Boot
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]