package br.inf.softhausit.zenite.zenmei.bff.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import br.inf.softhausit.zenite.zenmei.dto.DashboardMeiResponse;

/**
 * Feign Client para Dashboard API
 * 
 * @author JamesCoder
 */
@FeignClient(
    name = "dashboard-service",
    url = "${microservices.mei-api.url}",
    configuration = br.inf.softhausit.zenite.zenmei.bff.config.FeignConfig.class
)
public interface DashboardClient {

    @GetMapping("/api/v1/mei/{idMei}/dashboard")
    ResponseEntity<DashboardMeiResponse> getDashboard(@PathVariable UUID idMei);
}
