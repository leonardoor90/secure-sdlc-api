# Estágio 1: Build da aplicação
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
# Baixa as dependências (ajuda no cache do Docker)
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Estágio 2: Imagem final (leve e segura)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Cria um usuário não-root para segurança (boa prática DevSecOps)
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
# Copia apenas o .jar gerado no estágio anterior
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
# Inicia a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
