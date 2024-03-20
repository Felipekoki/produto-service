####
# Este Dockerfile é usado para construir um contêiner que executa a aplicação Spring Boot
#
# Construa a imagem com:
#
# docker build -f Dockerfile -t springboot/produto-service .
#
# Em seguida, execute o contêiner usando:
#
# docker run -i --rm -p 8081:8081 springboot/produto-service
# docker run --name=produto-service --network=dokcer_monitoramento-network -e "GRPC_LOG_RECORD_EXPORT_ENDPOINT=http://otel-collector:4317" -e "OTLP_METRICS_EXPORT_URL=http://otel-collector:4318/v1/metrics" -e "OTLP_TRACING_ENDPOINT=http://otel-collector::4318/v1/traces" -i --rm -p 8081:8081 springboot/produto-service

####

# Estágio de construção
FROM registry.access.redhat.com/ubi8/openjdk-17:1.15-1.1682053058 AS builder

# Construir dependência offline para otimizar a construção
RUN mkdir project
WORKDIR /home/jboss/project
COPY pom.xml .
RUN mvn clean
RUN mvn dependency:go-offline

COPY src src
RUN mvn package -Dmaven.test.skip=true
# Calcula o nome do jar criado e coloca-o em um local conhecido para copiar para o próximo estágio.
# Se o usuário alterar o pom.xml para ter uma versão diferente ou artifactId, isso encontrará o jar
RUN grep version target/maven-archiver/pom.properties | cut -d '=' -f2 > .env-version
RUN grep artifactId target/maven-archiver/pom.properties | cut -d '=' -f2 > .env-id
RUN mv target/$(cat .env-id)-$(cat .env-version).jar target/export-run-artifact.jar

# Estágio de execução
FROM registry.access.redhat.com/ubi8/openjdk-17-runtime:1.15-1.1682053056

# Copiar o artefato construído do estágio anterior para o estágio de execução
COPY --from=builder /home/jboss/project/target/export-run-artifact.jar  /deployments/export-run-artifact.jar

# Expor a porta em que a aplicação será executada
EXPOSE 8081

# Comando para iniciar a aplicação Spring Boot quando o contêiner for iniciado
ENTRYPOINT ["/opt/jboss/container/java/run/run-java.sh", "--server.port=8081"]
