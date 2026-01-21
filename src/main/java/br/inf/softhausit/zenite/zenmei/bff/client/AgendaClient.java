package br.inf.softhausit.zenite.zenmei.bff.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Feign Client para Agenda API
 * <p>
 * Cliente para comunicação com o microserviço de agendamentos
 * </p>
 */
@FeignClient(
    name = "agenda-service",
    url = "${microservices.agenda-api.url}",
    configuration = br.inf.softhausit.zenite.zenmei.bff.config.FeignConfig.class
)
public interface AgendaClient {

    @GetMapping("/api/v1/compromissos")
    ResponseEntity<?> listarCompromissos(@RequestHeader("X-User-Id") UUID userId);

    @GetMapping("/api/v1/compromissos/{id}")
    ResponseEntity<?> buscarCompromisso(@PathVariable("id") UUID id);

    @PostMapping("/api/v1/compromissos")
    ResponseEntity<?> criarCompromisso(@RequestBody Object compromisso, @RequestHeader("X-User-Id") UUID userId);

    @PutMapping("/api/v1/compromissos/{id}")
    ResponseEntity<?> atualizarCompromisso(@PathVariable("id") UUID id, @RequestBody Object compromisso);

    @DeleteMapping("/api/v1/compromissos/{id}")
    ResponseEntity<Void> deletarCompromisso(@PathVariable("id") UUID id);
}
