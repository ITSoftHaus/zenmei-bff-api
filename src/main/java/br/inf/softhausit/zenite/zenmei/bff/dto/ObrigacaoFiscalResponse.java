package br.inf.softhausit.zenite.zenmei.bff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Response DTO for fiscal obligation information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ObrigacaoFiscalResponse {
    
    private UUID id;
    private UUID idMei;
    private UUID idObrigacao;
    private String obrigacao;
    private String diaCompetencia;
    private String mesAnoCompetencia;
    private String status;
    private String pdfRelatorio;
}
