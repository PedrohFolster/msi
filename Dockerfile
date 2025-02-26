# Imagem base com JDK 17
FROM eclipse-temurin:17-jdk-focal

# Diretório de trabalho
WORKDIR /app

# Copiar o arquivo pom.xml
COPY pom.xml .

# Copiar o código fonte e recursos
COPY src ./src

# Instalar o Maven e construir o projeto
RUN apt-get update && \
    apt-get install -y maven && \
    mvn clean package -DskipTests

# Expor a porta 8080
EXPOSE 8080

# Comando para executar a aplicação
CMD ["java", "-jar", "target/msi-0.0.1-SNAPSHOT.jar"] 