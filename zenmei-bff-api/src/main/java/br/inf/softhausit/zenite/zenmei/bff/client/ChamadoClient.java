package br.inf.softhausit.zenite.zenmei.bff.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Feign Client para Chamado API
 * <p>
 * Cliente para comunicação com o microserviço de chamados
 * </p>
 */
@FeignClient(
    name = "chamado-service",
    url = "${microservices.chamado-api.url}",
    configuration = br.inf.softhausit.zenite.zenmei.bff.config.FeignConfig.class
)
public interface ChamadoClient {

    @GetMapping("/api/v1/chamados")
    ResponseEntity<?> listarChamados(@RequestHeader("X-User-Id") UUID userId);

    @GetMapping("/api/v1/chamados/{id}")
    ResponseEntity<?> buscarChamado(@PathVariable("id") UUID id);

    @PostMapping("/api/v1/chamados")
    ResponseEntity<?> criarChamado(@RequestBody Object chamado, @RequestHeader("X-User-Id") UUID userId);

    @PutMapping("/api/v1/chamados/{id}")
    ResponseEntity<?> atualizarChamado(@PathVariable("id") UUID id, @RequestBody Object chamado);

    @DeleteMapping("/api/v1/chamados/{id}")
    ResponseEntity<Void> deletarChamado(@PathVariable("id") UUID id);
}
