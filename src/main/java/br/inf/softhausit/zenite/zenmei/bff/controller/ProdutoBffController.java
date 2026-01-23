package br.inf.softhausit.zenite.zenmei.bff.controller;

import br.inf.softhausit.zenite.zenmei.bff.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controller BFF para operações de Produtos
 */
@Slf4j
@RestController
@RequestMapping("/api/bff/v1/produto")
@RequiredArgsConstructor
@Tag(name = "Produtos", description = "APIs de gerenciamento de produtos")
public class ProdutoBffController {

    private final ProdutoService produtoService;

    @Operation(summary = "Listar todos os produtos")
    @GetMapping
    public ResponseEntity<?> listarProdutos(@RequestHeader("X-User-Id") UUID userId) {
        log.info("BFF: Listando produtos para userId: {}", userId);
        return produtoService.listarProdutos(userId);
    }

    @Operation(summary = "Buscar produto por ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarProduto(@PathVariable UUID id) {
        log.info("BFF: Buscando produto: {}", id);
        return produtoService.buscarProduto(id);
    }

    @Operation(summary = "Criar novo produto")
    @PostMapping
    public ResponseEntity<?> criarProduto(
            @RequestBody Object produto,
            @RequestHeader("X-User-Id") UUID userId) {
        log.info("BFF: Criando novo produto");
        return produtoService.criarProduto(produto, userId);
    }

    @Operation(summary = "Atualizar produto")
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarProduto(
            @PathVariable UUID id,
            @RequestBody Object produto) {
        log.info("BFF: Atualizando produto: {}", id);
        return produtoService.atualizarProduto(id, produto);
    }

    @Operation(summary = "Deletar produto")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable UUID id) {
        log.info("BFF: Deletando produto: {}", id);
        return produtoService.deletarProduto(id);
    }
}
