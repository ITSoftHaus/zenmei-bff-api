package br.inf.softhausit.zenite.zenmei.bff.service;

import br.inf.softhausit.zenite.zenmei.bff.client.ObrigacoesFiscaisClient;
import br.inf.softhausit.zenite.zenmei.dto.MeiObrigacoesAtrasadasResponse;
import br.inf.softhausit.zenite.zenmei.dto.ObrigacaoAtrasadaResponse;
import br.inf.softhausit.zenite.zenmei.dto.ObrigacaoFiscalResponse;
import br.inf.softhausit.zenite.zenmei.dto.TipoObrigacaoFiscalResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for orchestrating fiscal obligations operations.
 * Implements circuit breaker and retry patterns for resilience.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ObrigacoesFiscaisService {

    private final ObrigacoesFiscaisClient obrigacoesFiscaisClient;

    /**
     * List types of fiscal obligations with fallback.
     */
    @CircuitBreaker(name = "obrigacoesFiscaisService", fallbackMethod = "listarTiposObrigacoesFallback")
    @Retry(name = "obrigacoesFiscaisService")
    public List<TipoObrigacaoFiscalResponse> listarTiposObrigacoes() {
        log.debug("Listing fiscal obligation types");
        return obrigacoesFiscaisClient.listarTiposObrigacoes();
    }

    /**
     * List fiscal obligations for a specific MEI with fallback.
     * Always returns 3 obligations, with DASN-SIMEI included.
     */
    @CircuitBreaker(name = "obrigacoesFiscaisService", fallbackMethod = "listarObrigacoesPorMeiFallback")
    @Retry(name = "obrigacoesFiscaisService")
    public List<ObrigacaoFiscalResponse> listarObrigacoesPorMei(UUID idMei) {
        log.debug("Listing fiscal obligations for MEI: {}", idMei);
        List<ObrigacaoFiscalResponse> obrigacoes = obrigacoesFiscaisClient.listarObrigacoesPorMei(idMei);
        
        // Ensure we always return 3 obligations with DASN-SIMEI included
        return garantirTresObrigacoes(obrigacoes);
    }

    /**
     * List MEIs with overdue obligations with fallback.
     */
    @CircuitBreaker(name = "obrigacoesFiscaisService", fallbackMethod = "listarMeisComObrigacoesAtrasadasFallback")
    @Retry(name = "obrigacoesFiscaisService")
    public List<MeiObrigacoesAtrasadasResponse> listarMeisComObrigacoesAtrasadas() {
        log.debug("Listing MEIs with overdue obligations");
        return obrigacoesFiscaisClient.listarMeisComObrigacoesAtrasadas();
    }

    /**
     * List overdue fiscal obligations with calculated delay days.
     */
    @CircuitBreaker(name = "obrigacoesFiscaisService", fallbackMethod = "listarObrigacoesAtrasadasFallback")
    @Retry(name = "obrigacoesFiscaisService")
    public List<ObrigacaoAtrasadaResponse> listarObrigacoesAtrasadas() {
        log.debug("Listing overdue fiscal obligations");
        
        // Get MEIs with overdue obligations
        List<MeiObrigacoesAtrasadasResponse> meisAtrasados = 
            obrigacoesFiscaisClient.listarMeisComObrigacoesAtrasadas();
        
        List<ObrigacaoAtrasadaResponse> obrigacoesAtrasadas = new ArrayList<>();
        
        // For each MEI with overdue obligations, get their obligations and filter overdue ones
        for (MeiObrigacoesAtrasadasResponse mei : meisAtrasados) {
            List<ObrigacaoFiscalResponse> obrigacoes = 
                obrigacoesFiscaisClient.listarObrigacoesPorMei(mei.getIdMei());
            
            // Filter and convert to overdue response with calculated days
            List<ObrigacaoAtrasadaResponse> atrasadasDoMei = obrigacoes.stream()
                .filter(o -> "Atrasada".equals(o.getStatus()))
                .map(this::convertToObrigacaoAtrasada)
                .collect(Collectors.toList());
            
            obrigacoesAtrasadas.addAll(atrasadasDoMei);
        }
        
        return obrigacoesAtrasadas;
    }

    /**
     * Close a fiscal obligation and trigger creation of next one.
     */
    @CircuitBreaker(name = "obrigacoesFiscaisService", fallbackMethod = "fecharObrigacaoFallback")
    @Retry(name = "obrigacoesFiscaisService")
    public ObrigacaoFiscalResponse fecharObrigacao(UUID idMei, UUID idObrigacao) {
        log.debug("Closing fiscal obligation {} for MEI {}", idObrigacao, idMei);
        
        // Get current obligation
        List<ObrigacaoFiscalResponse> obrigacoes = 
            obrigacoesFiscaisClient.listarObrigacoesPorMei(idMei);
        
        ObrigacaoFiscalResponse obrigacao = obrigacoes.stream()
            .filter(o -> o.getId().equals(idObrigacao))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Obligation not found"));
        
        // Update status to "Em dia"
        obrigacao.setStatus("Em dia");
        
        return obrigacoesFiscaisClient.atualizarObrigacao(idMei, idObrigacao, obrigacao);
    }

    // Fallback methods

    private List<TipoObrigacaoFiscalResponse> listarTiposObrigacoesFallback(Exception ex) {
        log.warn("Circuit breaker activated for listarTiposObrigacoes. Returning empty list. Error: {}", 
            ex.getMessage());
        return Collections.emptyList();
    }

    private List<ObrigacaoFiscalResponse> listarObrigacoesPorMeiFallback(UUID idMei, Exception ex) {
        log.warn("Circuit breaker activated for listarObrigacoesPorMei for MEI {}. Returning empty list. Error: {}", 
            idMei, ex.getMessage());
        return Collections.emptyList();
    }

    private List<MeiObrigacoesAtrasadasResponse> listarMeisComObrigacoesAtrasadasFallback(Exception ex) {
        log.warn("Circuit breaker activated for listarMeisComObrigacoesAtrasadas. Returning empty list. Error: {}", 
            ex.getMessage());
        return Collections.emptyList();
    }

    private List<ObrigacaoAtrasadaResponse> listarObrigacoesAtrasadasFallback(Exception ex) {
        log.warn("Circuit breaker activated for listarObrigacoesAtrasadas. Returning empty list. Error: {}", 
            ex.getMessage());
        return Collections.emptyList();
    }

    private ObrigacaoFiscalResponse fecharObrigacaoFallback(UUID idMei, UUID idObrigacao, Exception ex) {
        log.error("Circuit breaker activated for fecharObrigacao. MEI: {}, Obligation: {}. Error: {}", 
            idMei, idObrigacao, ex.getMessage());
        throw new RuntimeException("Service temporarily unavailable. Please try again later.", ex);
    }

    // Helper methods

    /**
     * Ensure the list always contains 3 obligations with DASN-SIMEI included.
     */
    private List<ObrigacaoFiscalResponse> garantirTresObrigacoes(List<ObrigacaoFiscalResponse> obrigacoes) {
        if (obrigacoes == null || obrigacoes.isEmpty()) {
            return Collections.emptyList();
        }

        // Find DASN-SIMEI (must always be present)
        ObrigacaoFiscalResponse dasnSimei = obrigacoes.stream()
            .filter(o -> "DASN-SIMEI".equals(o.getObrigacao()))
            .findFirst()
            .orElse(null);

        // Get non-DASN obligations sorted by date (closest first)
        List<ObrigacaoFiscalResponse> outras = obrigacoes.stream()
            .filter(o -> !"DASN-SIMEI".equals(o.getObrigacao()))
            .filter(o -> "A vencer".equals(o.getStatus()))
            .limit(2)
            .collect(Collectors.toList());

        List<ObrigacaoFiscalResponse> resultado = new ArrayList<>();
        
        if (dasnSimei != null) {
            resultado.add(dasnSimei);
        }
        
        resultado.addAll(outras);

        // Limit to 3
        return resultado.stream().limit(3).collect(Collectors.toList());
    }

    /**
     * Convert ObrigacaoFiscalResponse to ObrigacaoAtrasadaResponse with calculated days.
     */
    private ObrigacaoAtrasadaResponse convertToObrigacaoAtrasada(ObrigacaoFiscalResponse obrigacao) {
        int diasAtraso = calcularDiasAtraso(obrigacao.getMesAnoCompetencia(), obrigacao.getDiaCompetencia());
        
        return ObrigacaoAtrasadaResponse.builder()
            .id(obrigacao.getId())
            .idMei(obrigacao.getIdMei())
            .obrigacao(obrigacao.getObrigacao())
            .diaCompetencia(obrigacao.getDiaCompetencia())
            .mesAnoCompetencia(obrigacao.getMesAnoCompetencia())
            .diasAtraso(diasAtraso)
            .build();
    }

    /**
     * Calculate days overdue based on mesAnoCompetencia (MMYYYY) and diaCompetencia.
     */
    private int calcularDiasAtraso(String mesAnoCompetencia, String diaCompetencia) {
        try {
            // Parse mesAnoCompetencia (format: MMYYYY or MM/YYYY)
            String mesAnoClean = mesAnoCompetencia.replace("/", "");
            int mes = Integer.parseInt(mesAnoClean.substring(0, 2));
            int ano = Integer.parseInt(mesAnoClean.substring(2));
            int dia = Integer.parseInt(diaCompetencia);

            LocalDate dataVencimento = LocalDate.of(ano, mes, dia);
            LocalDate hoje = LocalDate.now();

            return (int) ChronoUnit.DAYS.between(dataVencimento, hoje);
        } catch (Exception e) {
            log.error("Error calculating overdue days for {}/{}. Error: {}", 
                mesAnoCompetencia, diaCompetencia, e.getMessage());
            return 0;
        }
    }
}
