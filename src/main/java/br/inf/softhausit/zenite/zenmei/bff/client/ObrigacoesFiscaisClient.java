package br.inf.softhausit.zenite.zenmei.bff.client;

import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.inf.softhausit.zenite.zenmei.dto.MeiObrigacoesAtrasadasResponse;
import br.inf.softhausit.zenite.zenmei.dto.ObrigacaoFiscalResponse;
import br.inf.softhausit.zenite.zenmei.dto.TipoObrigacaoFiscalResponse;

/**
 * Feign client for fiscal obligations API.
 * Connects to zenmei-mei-api microservice.
 */
@FeignClient(
	    name = "mei-obrigacoes-service",
	    url = "${microservices.mei-api.url}",
	    configuration = br.inf.softhausit.zenite.zenmei.bff.config.FeignConfig.class
	)
public interface ObrigacoesFiscaisClient {

    /**
     * List all types of fiscal obligations.
     */
    @GetMapping("/api/v1/mei/obrigacoes-fiscais/tipos")
    List<TipoObrigacaoFiscalResponse> listarTiposObrigacoes();

    /**
     * List fiscal obligations for a specific MEI.
     */
    @GetMapping("/api/v1/mei/{idMei}/obrigacoes-fiscais")
    List<ObrigacaoFiscalResponse> listarObrigacoesPorMei(@PathVariable UUID idMei);

    /**
     * Create a new fiscal obligation for a MEI.
     */
    @PostMapping("/api/v1/mei/{idMei}/obrigacoes-fiscais")
    ObrigacaoFiscalResponse criarObrigacao(
        @PathVariable UUID idMei,
        @RequestBody ObrigacaoFiscalResponse obrigacao);

    /**
     * Update an existing fiscal obligation.
     */
    @PutMapping("/api/v1/mei/{idMei}/obrigacoes-fiscais/{id}")
    ObrigacaoFiscalResponse atualizarObrigacao(
        @PathVariable UUID idMei,
        @PathVariable UUID id,
        @RequestBody ObrigacaoFiscalResponse obrigacao
    );
    
    /**
     * List MEIs with overdue obligations.
     */
    @GetMapping("/api/v1/mei/obrigacoes-atrasadas")
    List<MeiObrigacoesAtrasadasResponse> listarMeisComObrigacoesAtrasadas();
    
    /**
     * Close a fiscal obligation.
     */
    @PostMapping("/api/v1/mei/{idMei}/obrigacoes-fiscais/{id}/fechar")
    ObrigacaoFiscalResponse fecharObrigacao(
        @PathVariable UUID idMei,
        @PathVariable UUID id
    );

}
