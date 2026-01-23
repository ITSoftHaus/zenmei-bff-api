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
 * Feign Client para User API
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
    
    @GetMapping("/email/{email}")
    ResponseEntity<?> getMeiByEmail(@NonNull @PathVariable String email);
    
    @GetMapping("/cpf/{cpf}")
	public ResponseEntity<?> getMeiByCpf(@NonNull @PathVariable String cpf);
    
    @GetMapping("/cnpj/{cnpj}")
	public ResponseEntity<?> getMeiByCnpj(@NonNull @PathVariable String cnpj);

    @PutMapping("/api/v1/mei")
    ResponseEntity<?> criarMei(@RequestBody Object mei);   
    
    @DeleteMapping("/api/v1/mei/{id}")
    ResponseEntity<Void> deletarMei(@PathVariable("id") UUID id);
    
    @GetMapping("/obrigacoes")
	public ResponseEntity<List<?>> getAllObrigacoesFiscais();
    
    @GetMapping("/obrigacoes/{idMei}")
	public ResponseEntity<List<?>> getAllMeiObrigacoesFiscais(@NonNull @PathVariable UUID idMei);
    
    @GetMapping("/{idMei}/obrigacoes-atrasadas")
    public ResponseEntity<List<?>> listarObrigacoesAtrasadasMei(@PathVariable @NonNull UUID idMei);
    
    @PostMapping("/obrigacoes/criar")
	public ResponseEntity<List<?>> criaMeiObrigacoesMeiProfile(@NonNull @RequestBody List<Object> meiObrigacoesFiscais);
    
    @PutMapping("/obrigacoes/alterar")
	public ResponseEntity<?> alterarMeiObrigacoesMeiProfile(@NonNull @RequestBody Object meiObrigacoesFiscais);
}
