package br.inf.softhausit.zenite.zenmei.bff.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuração do OpenAPI/Swagger
 */
@Configuration
public class OpenApiConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ZenMEI BFF API")
                        .version("1.0.0")
                        .description("""
                                Backend for Frontend - Camada de integração para todos os microsserviços do ZenMEI.
                                
                                Este BFF agrega e orquestra chamadas aos microsserviços backend, implementando
                                padrões de resiliência como Circuit Breaker, Retry e Timeout.
                                
                                ## Características
                                - ✅ Agregação de múltiplos microsserviços
                                - ✅ Circuit Breaker com Resilience4j
                                - ✅ Retry automático com backoff exponencial
                                - ✅ Propagação automática de headers (Authorization, X-User-Id)
                                - ✅ Rastreamento distribuído com X-Request-Id
                                - ✅ Métricas Prometheus
                                - ✅ Health checks detalhados
                                
                                ## Microsserviços Integrados
                                - User API (8081)
                                - Agenda API (8082)
                                - Chamado API (8084)
                                - Client API (8085)
                                - CNAE API (8086)
                                - Despesa API (8087)
                                - Nota API (8088)
                                - Receita API (8089)
                                - Serviço API (8090)
                                - Produto API (8091)
                                """)
                        .contact(new Contact()
                                .name("ZenMEI Development Team")
                                .email("dev@softhausit.com.br")
                                .url("https://softhausit.com.br"))
                        .license(new License()
                                .name("Proprietary")
                                .url("https://softhausit.com.br/license")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Ambiente Local"),
                        new Server()
                                .url("https://api-dev.zenmei.com.br")
                                .description("Ambiente de Desenvolvimento"),
                        new Server()
                                .url("https://api.zenmei.com.br")
                                .description("Ambiente de Produção")
                ));
    }
}
