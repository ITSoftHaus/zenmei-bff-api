package br.inf.softhausit.zenite.zenmei.bff.integration;

import br.inf.softhausit.zenite.zenmei.model.Mei;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MeiBffIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void healthCheck_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/bff/v1/health"))
                .andExpect(status().isOk());
    }

    @Test
    void getMeis_WithValidHeader_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/bff/v1/meis")
                        .header("X-User-Id", "test-user-123"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void createMei_WithValidData_ShouldReturnCreated() throws Exception {
        Mei mei = new Mei();
        mei.setNome("Test User");
        mei.setEmail("test@example.com");
        mei.setCnpj("12345678000190");

        mockMvc.perform(post("/api/bff/v1/meis")
                        .header("X-User-Id", "test-user-123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mei)))
                .andExpect(status().isCreated());
    }

    @Test
    void getMeiById_WithInvalidId_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/bff/v1/meis/invalid-id")
                        .header("X-User-Id", "test-user-123"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateMei_WithValidData_ShouldReturnOk() throws Exception {
        Mei mei = new Mei();
        mei.setId("1");
        mei.setNome("Updated Name");

        mockMvc.perform(put("/api/bff/v1/meis/1")
                        .header("X-User-Id", "test-user-123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mei)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteMei_WithValidId_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/bff/v1/meis/1")
                        .header("X-User-Id", "test-user-123"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getMeiByEmail_WithValidEmail_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/bff/v1/meis/email/test@example.com"))
                .andExpect(status().isOk());
    }

    @Test
    void getMeiByCpf_WithValidCpf_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/bff/v1/meis/cpf/12345678901"))
                .andExpect(status().isOk());
    }

    @Test
    void getMeiByCnpj_WithValidCnpj_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/bff/v1/meis/cnpj/12345678000190"))
                .andExpect(status().isOk());
    }

    @Test
    void withoutUserIdHeader_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/bff/v1/meis"))
                .andExpect(status().isBadRequest());
    }
}
