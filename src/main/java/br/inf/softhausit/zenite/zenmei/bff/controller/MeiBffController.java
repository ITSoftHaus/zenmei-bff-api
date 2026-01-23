package br.inf.softhausit.zenite.zenmei.bff.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.inf.softhausit.zenite.zenmei.bff.dto.MeiObrigacoesAtrasadasResponse;
import br.inf.softhausit.zenite.zenmei.bff.dto.ObrigacaoFiscalResponse;
import br.inf.softhausit.zenite.zenmei.bff.dto.TipoObrigacaoFiscalResponse;
import br.inf.softhausit.zenite.zenmei.bff.service.MeiService;
import br.inf.softhausit.zenite.zenmei.bff.service.ObrigacoesFiscaisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller BFF para operações de MEI
 * <p>
 * Agrega e expõe endpoints do microserviço de MEI
 * </p>
 */
@Slf4j
@RestController
@RequestMapping("/api/bff/v1/mei")
@RequiredArgsConstructor
@Tag(name = "Meis", description = "APIs de gerenciamento de MEI")
public class MeiBffController {

    private final MeiService meiService;
    
    private final ObrigacoesFiscaisService obrigacoesFiscaisService;

    @Operation(summary = "Listar todos os MEIs", description = "Retorna lista de MEIs do sistema")
    @GetMapping
    public ResponseEntity<?> listarMeis(
            @Parameter(description = "ID do usuário autenticado", required = true)
            @RequestHeader("X-User-Id") UUID userId) {
        log.info("BFF: Listando MEIs para userId: {}", userId);
        return meiService.listarMeis(userId);
    }

    @Operation(summary = "Buscar MEI por ID", description = "Retorna detalhes de um MEI específico")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarMei(
            @Parameter(description = "ID do MEI", required = true)
            @PathVariable UUID id) {
        log.info("BFF: Buscando MEI: {}", id);
        return meiService.buscarMei(id);
    }
    
    @Operation(summary = "Buscar MEI por Email", description = "Retorna detalhes de um MEI específico")
    @GetMapping("/email/{email}")
    public ResponseEntity<?> buscarMeiPorEmail(
            @Parameter(description = "Email do MEI", required = true)
            @PathVariable String email) {
        log.info("BFF: Buscando MEI por email: {}", email);
        return meiService.buscarMeiByMail(email);
    }
    
    @Operation(summary = "Buscar MEI por CPF", description = "Retorna detalhes de um MEI específico")
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<?> buscarMeiPorCpf(
            @Parameter(description = "CPF do MEI", required = true)
            @PathVariable String cpf) {
        log.info("BFF: Buscando MEI por CPF: {}", cpf);
        return meiService.buscarMeiByCpf(cpf);
    }
    
    @Operation(summary = "Buscar MEI por CNPJ", description = "Retorna detalhes de um MEI específico")
    @GetMapping("/cnpj/{cnpj}")
    public ResponseEntity<?> buscarMeiPorCnpj(
            @Parameter(description = "CNPJ do MEI", required = true)
            @PathVariable String cnpj) {
        log.info("BFF: Buscando MEI por CNPJ: {}", cnpj);
        return meiService.buscarMeiByCnpj(cnpj);
    }

    @Operation(summary = "Criar novo MEI", description = "Cria um novo MEI no sistema")
    @PostMapping
    public ResponseEntity<?> criarMei(@RequestBody Object mei) {
        log.info("BFF: Criando novo MEI");
        return meiService.criarMei(mei);
    }
    
    @Operation(summary = "Atualizar MEI", description = "Atualiza dados de um MEI existente")
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarMei(
            @Parameter(description = "ID do MEI", required = true)
            @PathVariable UUID id,
            @RequestBody Object mei) {
        log.info("BFF: Atualizando MEI: {}", id);
        return meiService.atualizarMei(id, mei);
    }

    @Operation(summary = "Deletar MEI", description = "Remove um MEI do sistema")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarMei(
            @Parameter(description = "ID do MEI", required = true)
            @PathVariable UUID id) {
        log.info("BFF: Deletando MEI: {}", id);
        return meiService.deletarMei(id);
    }    
    
    @GetMapping("/obrigacoes-fiscais/tipos")
    public ResponseEntity<List<TipoObrigacaoFiscalResponse>> listarTiposObrigacoes() {
        log.info("Request to list fiscal obligation types");
        List<TipoObrigacaoFiscalResponse> tipos = obrigacoesFiscaisService.listarTiposObrigacoes();
        return ResponseEntity.ok(tipos);
    }
    
    @GetMapping("/{idMei}/obrigacoes-fiscais")
    public ResponseEntity<List<ObrigacaoFiscalResponse>> listarObrigacoesPorMei(
            @PathVariable UUID idMei) {
        log.info("Request to list fiscal obligations for MEI: {}", idMei);
        List<ObrigacaoFiscalResponse> obrigacoes = 
            obrigacoesFiscaisService.listarObrigacoesPorMei(idMei);
        return ResponseEntity.ok(obrigacoes);
    }
    
    @GetMapping("/{idMei}/obrigacoes-atrasadas")
    public ResponseEntity<List<MeiObrigacoesAtrasadasResponse>> listarObrigacoesAtrasadasMei(@PathVariable UUID idMei) {
        log.info("Request to list overdue obligations MEI");
        List<MeiObrigacoesAtrasadasResponse> meis = 
            obrigacoesFiscaisService.listarMeisComObrigacoesAtrasadas();
        return ResponseEntity.ok(meis);
    }
    
   
    @PostMapping("/{idMei}/obrigacoes-fiscais/{id}/fechar")
    public ResponseEntity<ObrigacaoFiscalResponse> fecharObrigacao(
            @PathVariable UUID idMei,
            @PathVariable UUID id) {
        log.info("Request to close fiscal obligation {} for MEI {}", id, idMei);
        ObrigacaoFiscalResponse obrigacao = 
            obrigacoesFiscaisService.fecharObrigacao(idMei, id);
        return ResponseEntity.ok(obrigacao);
    }
}
