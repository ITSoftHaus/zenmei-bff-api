package br.inf.softhausit.zenite.zenmei.bff.controller;

import br.inf.softhausit.zenite.zenmei.bff.client.MeiServiceClient;
import br.inf.softhausit.zenite.zenmei.model.Mei;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MeiBffControllerTest {

    @Mock
    private MeiServiceClient meiServiceClient;

    @InjectMocks
    private MeiBffController controller;

    private Mei mei;

    @BeforeEach
    void setUp() {
        mei = new Mei();
        mei.setId("1");
        mei.setNome("João Silva");
        mei.setEmail("joao@test.com");
        mei.setCnpj("12345678000190");
    }

    @Test
    void getAllMeis_ShouldReturnList() {
        List<Mei> meis = Arrays.asList(mei);
        when(meiServiceClient.getAllMeis(anyString())).thenReturn(ResponseEntity.ok(meis));

        ResponseEntity<List<Mei>> response = controller.getAllMeis("user-123");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        verify(meiServiceClient).getAllMeis("user-123");
    }

    @Test
    void getMeiById_ShouldReturnMei() {
        when(meiServiceClient.getMeiById(anyString(), anyString())).thenReturn(ResponseEntity.ok(mei));

        ResponseEntity<Mei> response = controller.getMeiById("1", "user-123");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo("1");
    }

    @Test
    void createMei_ShouldReturnCreated() {
        when(meiServiceClient.createMei(any(Mei.class), anyString())).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(mei));

        ResponseEntity<Mei> response = controller.createMei(mei, "user-123");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        verify(meiServiceClient).createMei(any(Mei.class), eq("user-123"));
    }

    @Test
    void updateMei_ShouldReturnOk() {
        when(meiServiceClient.updateMei(anyString(), any(Mei.class), anyString())).thenReturn(ResponseEntity.ok(mei));

        ResponseEntity<Mei> response = controller.updateMei("1", mei, "user-123");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(meiServiceClient).updateMei(eq("1"), any(Mei.class), eq("user-123"));
    }

    @Test
    void deleteMei_ShouldReturnNoContent() {
        when(meiServiceClient.deleteMei(anyString(), anyString())).thenReturn(ResponseEntity.noContent().build());

        ResponseEntity<Void> response = controller.deleteMei("1", "user-123");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(meiServiceClient).deleteMei("1", "user-123");
    }

    @Test
    void getMeiByEmail_ShouldReturnMei() {
        when(meiServiceClient.getMeiByEmail(anyString())).thenReturn(ResponseEntity.ok(mei));

        ResponseEntity<Mei> response = controller.getMeiByEmail("joao@test.com");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getEmail()).isEqualTo("joao@test.com");
    }

    @Test
    void getMeiByCpf_ShouldReturnMei() {
        when(meiServiceClient.getMeiByCpf(anyString())).thenReturn(ResponseEntity.ok(mei));

        ResponseEntity<Mei> response = controller.getMeiByCpf("12345678901");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getMeiByCnpj_ShouldReturnMei() {
        when(meiServiceClient.getMeiByCnpj(anyString())).thenReturn(ResponseEntity.ok(mei));

        ResponseEntity<Mei> response = controller.getMeiByCnpj("12345678000190");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getCnpj()).isEqualTo("12345678000190");
    }
}
