# Estágio de construção
FROM maven:3.8.4-openjdk-17 AS builder

# Diretório de trabalho
WORKDIR /app

# Copie o arquivo pom.xml para obter as dependências
COPY pom.xml .

# Copie o resto do código
COPY src src

# Construa o JAR
RUN mvn package -DskipTests

# Estágio de execução
FROM openjdk:17-jdk-alpine

# Diretório de trabalho
WORKDIR /app

# Copie o JAR construído do estágio de construção
COPY --from=builder /app/target/minha-carteira-0.0.1-SNAPSHOT.jar .

# Expõe a porta da sua aplicação (ajuste conforme necessário)
EXPOSE 8080

# Comando para executar a aplicação Spring
CMD ["java", "-jar", "minha-carteira-0.0.1-SNAPSHOT.jar"]