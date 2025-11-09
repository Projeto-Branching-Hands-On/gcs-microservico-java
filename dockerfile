# Multi-stage Dockerfile otimizado para aplicações Spring Boot
# Stage 1: build com Maven - produz o artefato jar
# Stage 2: runtime leve com JRE, usuário não-root e otimizações de camadas

########################################
# STAGE 1: Build (Maven)
########################################
FROM maven:3.9-openjdk-17 AS build

WORKDIR /workspace

# Copia apenas o pom.xml primeiro para aproveitar cache de dependências
COPY pom.xml ./
COPY mvnw ./
COPY .mvn .mvn

# Baixa dependências para cache (melhora tempo em builds incrementais)
RUN mvn -B -ntp dependency:go-offline

# Copia o restante do código e constrói o jar (skip tests para builds locais rápidos)
COPY src ./src
RUN mvn -B -ntp -DskipTests clean package


########################################
# STAGE 2: Runtime mínimo
########################################
FROM eclipse-temurin:17-jre-jammy AS runtime

ARG JAR_NAME=demo-0.0.1-SNAPSHOT.jar
WORKDIR /app

# Copia o jar gerado no estágio de build
COPY --from=build /workspace/target/${JAR_NAME} /app/app.jar

# Cria usuário não-root para rodar a aplicação (segurança)
RUN addgroup --system spring && adduser --system --ingroup spring spring || true
USER spring

# Porta padrão do Spring Boot
EXPOSE 8080

# Recomendações: passar variáveis de ambiente ao rodar (SPRING_PROFILES_ACTIVE, DATABASE_URL, etc.)
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

# Fim do Dockerfile
