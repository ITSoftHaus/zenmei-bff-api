package br.inf.softhausit.zenite.zenmei.bff.service;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.inf.softhausit.zenite.zenmei.bff.client.DashboardClient;
import br.inf.softhausit.zenite.zenmei.dto.DashboardMeiResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service para Dashboard (BFF)
 * 
 * @author JamesCoder
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardBffService {

    private final DashboardClient dashboardClient;

    @CircuitBreaker(name = "dashboardService", fallbackMethod = "getDashboardFallback")
    @Retry(name = "dashboardService")
    public ResponseEntity<DashboardMeiResponse> getDashboard(UUID idMei) {
        log.debug("Buscando dashboard para MEI: {}", idMei);
        return dashboardClient.getDashboard(idMei);
    }

    // Fallback method
    private ResponseEntity<DashboardMeiResponse> getDashboardFallback(UUID idMei, Exception e) {
        log.error("Fallback: Erro ao buscar dashboard do MEI {}", idMei, e);
        
        // Retornar dashboard vazio em caso de falha
        DashboardMeiResponse dashboardVazio = DashboardMeiResponse.builder()
            .resumoFinanceiro(null)
            .obrigacoesFiscais(null)
            .notasFiscais(null)
            .alertas(java.util.Collections.emptyList())
            .saudeFiscal(null)
            .build();
        
        return ResponseEntity.ok(dashboardVazio);
    }
}
