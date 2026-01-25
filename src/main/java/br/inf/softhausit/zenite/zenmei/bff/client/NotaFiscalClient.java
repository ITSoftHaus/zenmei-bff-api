package br.inf.softhausit.zenite.zenmei.bff.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Feign Client para Nota Fiscal API
 * <p>
 * Cliente para comunicação com o microserviço de notas fiscais
 * </p>
 */
@FeignClient(
    name = "nota-service",
    url = "${microservices.nota-api.url}",
    configuration = br.inf.softhausit.zenite.zenmei.bff.config.FeignConfig.class
)
public interface NotaFiscalClient {

    @GetMapping("/api/v1/nota")
    ResponseEntity<?> listarNotas(@RequestHeader("X-User-Id") UUID userId);

    @GetMapping("/api/v1/nota/{id}")
    ResponseEntity<?> buscarNota(@PathVariable("id") UUID id);

    @PostMapping("/api/v1/nota")
    ResponseEntity<?> criarNota(@RequestBody Object nota, @RequestHeader("X-User-Id") UUID userId);

    @PutMapping("/api/v1/nota/{id}")
    ResponseEntity<?> atualizarNota(@PathVariable("id") UUID id, @RequestBody Object nota);

    @DeleteMapping("/api/v1/nota/{id}")
    ResponseEntity<Void> deletarNota(@PathVariable("id") UUID id);

    @PostMapping("/api/v1/nota/{id}/emitir")
    ResponseEntity<?> emitirNota(@PathVariable("id") UUID id);
}
