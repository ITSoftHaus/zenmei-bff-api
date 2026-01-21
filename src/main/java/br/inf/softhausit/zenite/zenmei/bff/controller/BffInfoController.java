package br.inf.softhausit.zenite.zenmei.bff.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller para informações e health check do BFF
 */
@Slf4j
@RestController
@RequestMapping("/api/bff/v1")
@Tag(name = "BFF Info", description = "Informações e status do BFF")
public class BffInfoController {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${server.port}")
    private String serverPort;

    @Operation(summary = "Informações do BFF", description = "Retorna informações sobre o BFF e microsserviços integrados")
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getInfo() {
        log.debug("Requisição de informações do BFF");

        Map<String, Object> info = new HashMap<>();
        info.put("application", applicationName);
        info.put("version", "1.0.0");
        info.put("port", serverPort);
        info.put("timestamp", LocalDateTime.now());
        info.put("description", "Backend for Frontend - ZenMEI Integration Layer");

        Map<String, Object> microservices = new HashMap<>();
        microservices.put("mei-api", Map.of("port", 8081, "endpoint", "/api/v1/meis"));
        microservices.put("agenda-api", Map.of("port", 8082, "endpoint", "/api/v1/compromissos"));
        microservices.put("chamado-api", Map.of("port", 8084, "endpoint", "/api/v1/chamados"));
        microservices.put("client-api", Map.of("port", 8085, "endpoint", "/api/v1/clients"));
        microservices.put("cnae-api", Map.of("port", 8086, "endpoint", "/api/v1/cnaes"));
        microservices.put("despesa-api", Map.of("port", 8087, "endpoint", "/api/v1/despesas"));
        microservices.put("nota-api", Map.of("port", 8088, "endpoint", "/api/v1/notas"));
        microservices.put("receita-api", Map.of("port", 8089, "endpoint", "/api/v1/vendas"));
        microservices.put("servico-api", Map.of("port", 8090, "endpoint", "/api/v1/services"));
        microservices.put("produto-api", Map.of("port", 8091, "endpoint", "/api/v1/produtos"));

        info.put("microservices", microservices);

        Map<String, String> features = new HashMap<>();
        features.put("circuit-breaker", "Resilience4j");
        features.put("retry", "Exponential Backoff");
        features.put("timeout", "5 seconds");
        features.put("metrics", "Prometheus");
        features.put("tracing", "X-Request-Id");

        info.put("features", features);

        return ResponseEntity.ok(info);
    }

    @Operation(summary = "Health Check Simples", description = "Verifica se o BFF está respondendo")
    @GetMapping("/ping")
    public ResponseEntity<Map<String, String>> ping() {
        log.debug("Ping recebido");
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "timestamp", LocalDateTime.now().toString(),
                "message", "BFF is running"
        ));
    }
}
