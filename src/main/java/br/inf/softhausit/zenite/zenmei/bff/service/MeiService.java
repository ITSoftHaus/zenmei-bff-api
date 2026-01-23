package br.inf.softhausit.zenite.zenmei.bff.service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import br.inf.softhausit.zenite.zenmei.bff.client.MeiClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service para integração com MEI API
 * <p>
 * Implementa Circuit Breaker e Retry patterns para resiliência
 * </p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MeiService {

    private final MeiClient meiClient;

    @CircuitBreaker(name = "meiService", fallbackMethod = "listarMeisFallback")
    @Retry(name = "meiService")
    public ResponseEntity<?> listarMeis(UUID userId) {
        log.debug("Listando MEIs para userId: {}", userId);
        return meiClient.listarMeis(userId);
    }

    @CircuitBreaker(name = "meiService", fallbackMethod = "buscarMeiFallback")
    @Retry(name = "meiService")
    public ResponseEntity<?> buscarMei(UUID id) {
        log.debug("Buscando MEI: {}", id);
        return meiClient.buscarMei(id);
    }
    
    @CircuitBreaker(name = "meiService", fallbackMethod = "buscarMeiPorEmailFallback")
    @Retry(name = "meiService")
    public ResponseEntity<?> buscarMeiByMail(String email) {
        log.debug("Buscando MEI por email: {}", email);
        return meiClient.getMeiByEmail(email);
    }
    
    @CircuitBreaker(name = "meiService", fallbackMethod = "buscarMeiPorCpfFallback")
    @Retry(name = "meiService")
    public ResponseEntity<?> buscarMeiByCpf(String cpf) {
        log.debug("Buscando MEI por CPF: {}", cpf);
        return meiClient.getMeiByCpf(cpf);
    }
    
    @CircuitBreaker(name = "meiService", fallbackMethod = "buscarMeiPorCnpjFallback")
    @Retry(name = "meiService")
    public ResponseEntity<?> buscarMeiByCnpj(String cnpj) {
        log.debug("Buscando MEI por CNPJ: {}", cnpj);
        return meiClient.getMeiByCnpj(cnpj);
    }

    @CircuitBreaker(name = "meiService", fallbackMethod = "criarMeiFallback")
    @Retry(name = "meiService")
    public ResponseEntity<?> criarMei(Object mei) {
        log.debug("Criando MEI");
        return meiClient.criarMei(mei);
    }
    
    @CircuitBreaker(name = "meiService", fallbackMethod = "deletarMeiFallback")
    @Retry(name = "meiService")
    public ResponseEntity<Void> deletarMei(UUID id) {
        log.debug("Deletando MEI: {}", id);
        return meiClient.deletarMei(id);
    }
    
    @CircuitBreaker(name = "meiService", fallbackMethod = "getAllObrigacoesFiscaisFallback")
    @Retry(name = "meiService")
    public ResponseEntity<List<?>> getAllObrigacoesFiscais() {
        log.debug("Buscando todas as obrigações fiscais das MEI no sistema");
        return meiClient.getAllObrigacoesFiscais();
    }
    
    @CircuitBreaker(name = "meiService", fallbackMethod = "getAllMeiObrigacoesFiscaissFallback")
    @Retry(name = "meiService")
    public ResponseEntity<List<?>> getAllMeiObrigacoesFiscais(@NonNull @PathVariable UUID idMei) {
        log.debug("Buscando todas as obrigações fiscais da MEI: {}", idMei);
        return meiClient.getAllMeiObrigacoesFiscais(idMei);
    }

    // Fallback methods
    @SuppressWarnings("unused")
	private ResponseEntity<?> listarMeisFallback(UUID userId, Exception e) {
        log.error("Fallback: Erro ao listar MEIs", e);
        return ResponseEntity.ok(Collections.emptyList());
    }

    @SuppressWarnings("unused")
	private ResponseEntity<?> buscarMeiFallback(UUID id, Exception e) {
        log.error("Fallback: Erro ao buscar MEI {}", id, e);
        return ResponseEntity.notFound().build();
    }
    
    @SuppressWarnings("unused")
	private ResponseEntity<?> buscarMeiPorEmailFallback(String email, Exception e) {
        log.error("Fallback: Erro ao buscar MEI pelo Email {}", email, e);
        return ResponseEntity.notFound().build();
    }
    
    @SuppressWarnings("unused")
	private ResponseEntity<?> buscarMeiPorCpfFallback(String cpf, Exception e) {
        log.error("Fallback: Erro ao buscar MEI pelo CPF {}", cpf, e);
        return ResponseEntity.notFound().build();
    }
    
    @SuppressWarnings("unused")
	private ResponseEntity<?> buscarMeiPorCnpjFallback(String cnpj, Exception e) {
        log.error("Fallback: Erro ao buscar MEI pelo CNPJ {}", cnpj, e);
        return ResponseEntity.notFound().build();
    }

    @SuppressWarnings("unused")
	private ResponseEntity<?> criarMeiFallback(Object mei, Exception e) {
        log.error("Fallback: Erro ao criar MEI", e);
        return ResponseEntity.status(503).build();
    }  

    @SuppressWarnings("unused")
	private ResponseEntity<Void> deletarMeiFallback(UUID id, Exception e) {
        log.error("Fallback: Erro ao deletar MEI {}", id, e);
        return ResponseEntity.status(503).build();
    }
    
    @SuppressWarnings("unused")
	private ResponseEntity<?> getAllObrigacoesFiscaisFallback(Exception e) {
        log.error("Fallback: Erro ao buscar Operações fiscais do sistema", e);
        return ResponseEntity.notFound().build();
    }
    
    @SuppressWarnings("unused")
   	private ResponseEntity<?> getAllMeiObrigacoesFiscaissFallback(UUID idMei, Exception e) {
           log.error("Fallback: Erro ao buscar obrigações fiscais do MEI: {}", idMei, e);
           return ResponseEntity.notFound().build();
    }
}
