package br.inf.softhausit.zenite.zenmei.bff.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Feign Client para Serviço API
 * <p>
 * Cliente para comunicação com o microserviço de serviços
 * </p>
 */
@FeignClient(
    name = "servico-service",
    url = "${microservices.servico-api.url}",
    configuration = br.inf.softhausit.zenite.zenmei.bff.config.FeignConfig.class
)
public interface ServicoClient {

    @GetMapping("/api/v1/servico")
    ResponseEntity<?> listarServicos(@RequestHeader("X-User-Id") UUID userId);

    @GetMapping("/api/v1/servico/{id}")
    ResponseEntity<?> buscarServico(@PathVariable("id") UUID id);

    @PostMapping("/api/v1/servico")
    ResponseEntity<?> criarServico(@RequestBody Object servico, @RequestHeader("X-User-Id") UUID userId);

    @PutMapping("/api/v1/servico")
    ResponseEntity<?> atualizarServico(@RequestBody Object servico);

    @DeleteMapping("/api/v1/servico/{id}")
    ResponseEntity<Void> deletarServico(@PathVariable("id") UUID id);
}
