package br.inf.softhausit.zenite.zenmei.bff.service;

import br.inf.softhausit.zenite.zenmei.bff.client.ServicoClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

/**
 * Service para integração com Servico API
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ServicoService {

    private final ServicoClient servicoClient;

    @CircuitBreaker(name = "servicoService", fallbackMethod = "listarServicosFallback")
    @Retry(name = "servicoService")
    public ResponseEntity<?> listarServicos(UUID userId) {
        log.debug("Listando serviços para userId: {}", userId);
        return servicoClient.listarServicos(userId);
    }

    @CircuitBreaker(name = "servicoService", fallbackMethod = "buscarServicoFallback")
    @Retry(name = "servicoService")
    public ResponseEntity<?> buscarServico(UUID id) {
        log.debug("Buscando serviço: {}", id);
        return servicoClient.buscarServico(id);
    }

    @CircuitBreaker(name = "servicoService", fallbackMethod = "criarServicoFallback")
    @Retry(name = "servicoService")
    public ResponseEntity<?> criarServico(Object servico, UUID userId) {
        log.debug("Criando serviço");
        return servicoClient.criarServico(servico, userId);
    }

    @CircuitBreaker(name = "servicoService", fallbackMethod = "atualizarServicoFallback")
    @Retry(name = "servicoService")
    public ResponseEntity<?> atualizarServico(Object servico) {
        log.debug("Atualizando serviço");
        return servicoClient.atualizarServico(servico);
    }

    @CircuitBreaker(name = "servicoService", fallbackMethod = "deletarServicoFallback")
    @Retry(name = "servicoService")
    public ResponseEntity<Void> deletarServico(UUID id) {
        log.debug("Deletando serviço: {}", id);
        return servicoClient.deletarServico(id);
    }

    // Fallback methods
    private ResponseEntity<?> listarServicosFallback(UUID userId, Exception e) {
        log.error("Fallback: Erro ao listar serviços", e);
        return ResponseEntity.ok(Collections.emptyList());
    }

    private ResponseEntity<?> buscarServicoFallback(UUID id, Exception e) {
        log.error("Fallback: Erro ao buscar serviço {}", id, e);
        return ResponseEntity.notFound().build();
    }

    private ResponseEntity<?> criarServicoFallback(Object servico, UUID userId, Exception e) {
        log.error("Fallback: Erro ao criar serviço", e);
        return ResponseEntity.status(503).build();
    }

    private ResponseEntity<?> atualizarServicoFallback(Object servico, Exception e) {
        log.error("Fallback: Erro ao atualizar serviço", e);
        return ResponseEntity.status(503).build();
    }

    private ResponseEntity<Void> deletarServicoFallback(UUID id, Exception e) {
        log.error("Fallback: Erro ao deletar serviço {}", id, e);
        return ResponseEntity.status(503).build();
    }
}
