package com.ia.smartkaufen;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.ollama.OllamaContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

    @Bean
    @ServiceConnection
    MongoDBContainer mongoDbContainer() {
        return new MongoDBContainer(DockerImageName.parse("mongo:latest"));
    }

    @Bean
    @ServiceConnection
    OllamaContainer ollamaContainer() {
        return new OllamaContainer(DockerImageName.parse("ollama/ollama:latest"));
    }

    @Bean
    @ServiceConnection
    MSSQLServerContainer<?> sqlServerContainer() {
        return new MSSQLServerContainer<>(DockerImageName.parse("mcr.microsoft.com/mssql/server:latest"));
    }

}
