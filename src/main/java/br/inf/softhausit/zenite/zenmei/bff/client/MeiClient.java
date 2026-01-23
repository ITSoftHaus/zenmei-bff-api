package br.inf.softhausit.zenite.zenmei.bff.client;

import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Feign Client para MEI API
 * <p>
 * Cliente para comunicação com o microserviço de MEI
 * </p>
 */
@FeignClient(
    name = "mei-service",
    url = "${microservices.mei-api.url}",
    configuration = br.inf.softhausit.zenite.zenmei.bff.config.FeignConfig.class
)
public interface MeiClient {

    @GetMapping("/api/v1/mei")
    ResponseEntity<?> listarMeis(@RequestParam("userId") UUID userId);

    @GetMapping("/api/v1/mei/{id}")
    ResponseEntity<?> buscarMei(@PathVariable("id") UUID id);
    
    @GetMapping("/api/v1/mei/email/{email}")
    ResponseEntity<?> getMeiByEmail(@NonNull @PathVariable String email);
    
    @GetMapping("/api/v1/mei/cpf/{cpf}")
	ResponseEntity<?> getMeiByCpf(@NonNull @PathVariable String cpf);
    
    @GetMapping("/api/v1/mei/cnpj/{cnpj}")
	ResponseEntity<?> getMeiByCnpj(@NonNull @PathVariable String cnpj);

    @PostMapping("/api/v1/mei")
    ResponseEntity<?> criarMei(@RequestBody Object mei);
    
    @PutMapping("/api/v1/mei/{id}")
    ResponseEntity<?> atualizarMei(@PathVariable("id") UUID id, @RequestBody Object mei);   
    
    @DeleteMapping("/api/v1/mei/{id}")
    ResponseEntity<Void> deletarMei(@PathVariable("id") UUID id);
    
    @GetMapping("/api/v1/mei/obrigacoes-fiscais/tipos")
	ResponseEntity<List<?>> getAllObrigacoesFiscais();
    
    @GetMapping("/api/v1/mei/{idMei}/obrigacoes-fiscais")
	ResponseEntity<List<?>> getAllMeiObrigacoesFiscais(@NonNull @PathVariable UUID idMei);
    
    @GetMapping("/api/v1/mei/{idMei}/obrigacoes-atrasadas")
    ResponseEntity<List<?>> listarObrigacoesAtrasadasMei(@PathVariable @NonNull UUID idMei);
    
    @PostMapping("/api/v1/mei/{idMei}/obrigacoes-fiscais")
	ResponseEntity<?> criarMeiObrigacaoFiscal(@PathVariable UUID idMei, @NonNull @RequestBody Object meiObrigacaoFiscal);
    
    @PutMapping("/api/v1/mei/{idMei}/obrigacoes-fiscais/{id}")
	ResponseEntity<?> atualizarMeiObrigacaoFiscal(@PathVariable UUID idMei, @PathVariable UUID id, @NonNull @RequestBody Object meiObrigacaoFiscal);
    
    @PostMapping("/api/v1/mei/{idMei}/obrigacoes-fiscais/{id}/fechar")
	ResponseEntity<?> fecharObrigacaoFiscal(@PathVariable UUID idMei, @PathVariable UUID id);
}
