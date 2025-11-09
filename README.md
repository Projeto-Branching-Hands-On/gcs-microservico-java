# GCS Microserviço Java

Uma base inicial para o microserviço Java do projeto GCS. Este README descreve como preparar o ambiente, construir o artefato e executá-lo via Docker.

## Sobre

Projeto microserviço Java com foco em deploy containerizado (Docker). O propósito deste repositório é prover uma base simples e reproduzível para desenvolvimento local, CI/CD e execução em ambientes conteinerizados.

## Tecnologias

- Java 17+ (ou versão compatível do projeto)
- Maven
- Docker

> Observação: ajuste versões conforme o pom.xml do projeto.

## Pré-requisitos

- Java JDK 17+ instalado (ou versão definida pelo projeto)
- Maven (opcional, se for construir localmente antes do Docker)
- Docker Engine instalado e rodando

## Como construir localmente

1. No diretório do projeto, construa o artefato com Maven:

	 mvn clean package -DskipTests

2. O jar gerado normalmente ficará em `target/` (ex.: `target/*.jar`).

## Executar localmente (sem Docker)

Depois de gerar o jar:

	 java -jar target/seu-artifact-id-version.jar

Substitua `seu-artifact-id-version.jar` pelo nome do artefato gerado.

## Rodar via Docker

Existem duas abordagens comuns:

1) Build da imagem a partir do jar local (recomendado quando se usa pipeline que gera o jar)

	 # construir a imagem (assumindo que o Dockerfile usa o jar em target/)
	 docker build -t gcs-microservico-java:latest .

	 # executar o container (substitua/adicione variáveis conforme necessário)
	 docker run --rm -p 8080:8080 \
		 -e SPRING_PROFILES_ACTIVE=local \
		 -e APP_ENV_VAR=value \
		 gcs-microservico-java:latest

2) Build multi-stage (recomenda-se adicionar um `Dockerfile` multi-stage ao projeto)

Exemplo simples de `Dockerfile` multi-stage:

```
FROM maven:3.9-openjdk-17 AS build
WORKDIR /workspace
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests clean package

FROM eclipse-temurin:17-jre-focal
WORKDIR /app
COPY --from=build /workspace/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]
```

Com esse Dockerfile basta rodar `docker build -t gcs-microservico-java:latest .` e depois `docker run` como mostrado acima.

## Variáveis de ambiente comuns

- SPRING_PROFILES_ACTIVE: perfil do Spring (ex.: local, dev, prod)
- SERVER_PORT: porta do servidor (caso a aplicação suporte)
- DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD: credenciais de banco (exemplos)

Inclua as variáveis que sua aplicação realmente usa no Docker run ou em um `docker-compose.yml`.

## Exemplo com docker-compose

```yaml
version: '3.8'
services:
	gcs-ms:
		image: gcs-microservico-java:latest
		build: .
		ports:
			- "8080:8080"
		environment:
			- SPRING_PROFILES_ACTIVE=local
			- DATABASE_URL=jdbc:postgresql://db:5432/gcs
		depends_on:
			- db

	db:
		image: postgres:15
		environment:
			- POSTGRES_DB=gcs
			- POSTGRES_USER=gcs
			- POSTGRES_PASSWORD=secret
```

## Testes

Rodar testes com Maven:

	 mvn test

## Boas práticas e próximos passos

- Adicionar um `Dockerfile` multi-stage se ainda não existir.
- Adicionar `docker-compose.override.yml` para facilitar cenários de desenvolvimento.
- Criar pipeline CI (GitHub Actions / GitLab CI) para build, test e push da imagem.
- Documentar variáveis de ambiente obrigatórias e endpoints expostos.

## Contribuindo

1. Abra uma issue descrevendo a sua sugestão ou bug.
2. Crie uma branch com escopo claro (ex.: feat/<descrição> ou fix/<descrição>).
3. Faça um PR com descrição e testes quando aplicável.

## Licença

Adicione aqui a licença do projeto (ex.: MIT, Apache-2.0) ou remova esta seção se não aplicável.

---

Se quiser, adapto o README para incluir instruções específicas do projeto (nome do jar, variáveis reais, exemplo de Health endpoints, ou integração com GCS). Diga o que prefere. 
