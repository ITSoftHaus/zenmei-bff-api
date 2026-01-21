package br.inf.softhausit.zenite.zenmei.bff.controller;

import br.inf.softhausit.zenite.zenmei.bff.service.ReceitaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controller BFF para operações de Receitas/Vendas
 */
@Slf4j
@RestController
@RequestMapping("/api/bff/v1/vendas")
@RequiredArgsConstructor
@Tag(name = "Receitas/Vendas", description = "APIs de gerenciamento de vendas e receitas")
public class ReceitaBffController {

    private final ReceitaService receitaService;

    @Operation(summary = "Listar todas as vendas")
    @GetMapping
    public ResponseEntity<?> listarVendas(@RequestHeader("X-User-Id") UUID userId) {
        log.info("BFF: Listando vendas para userId: {}", userId);
        return receitaService.listarVendas(userId);
    }

    @Operation(summary = "Buscar venda por ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarVenda(@PathVariable UUID id) {
        log.info("BFF: Buscando venda: {}", id);
        return receitaService.buscarVenda(id);
    }

    @Operation(summary = "Criar nova venda")
    @PostMapping
    public ResponseEntity<?> criarVenda(
            @RequestBody Object venda,
            @RequestHeader("X-User-Id") UUID userId) {
        log.info("BFF: Criando nova venda");
        return receitaService.criarVenda(venda, userId);
    }

    @Operation(summary = "Atualizar venda")
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarVenda(
            @PathVariable UUID id,
            @RequestBody Object venda) {
        log.info("BFF: Atualizando venda: {}", id);
        return receitaService.atualizarVenda(id, venda);
    }

    @Operation(summary = "Deletar venda")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarVenda(@PathVariable UUID id) {
        log.info("BFF: Deletando venda: {}", id);
        return receitaService.deletarVenda(id);
    }
}
