package br.inf.softhausit.zenite.zenmei.bff.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * Feign Client para Despesa API
 * <p>
 * Cliente para comunicação com o microserviço de despesas
 * </p>
 */
@FeignClient(
    name = "despesa-service",
    url = "${microservices.despesa-api.url}",
    configuration = br.inf.softhausit.zenite.zenmei.bff.config.FeignConfig.class
)
public interface DespesaClient {

    @GetMapping("/api/v1/despesas")
    ResponseEntity<?> listarDespesas(@RequestHeader("X-User-Id") UUID userId);

    @GetMapping("/api/v1/despesas/{id}")
    ResponseEntity<?> buscarDespesa(@PathVariable("id") UUID id);

    @PostMapping("/api/v1/despesas")
    ResponseEntity<?> criarDespesa(@RequestBody Object despesa, @RequestHeader("X-User-Id") UUID userId);

    @PutMapping("/api/v1/despesas/{id}")
    ResponseEntity<?> atualizarDespesa(@PathVariable("id") UUID id, @RequestBody Object despesa);

    @DeleteMapping("/api/v1/despesas/{id}")
    ResponseEntity<Void> deletarDespesa(@PathVariable("id") UUID id);
}
