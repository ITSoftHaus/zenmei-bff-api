package br.inf.softhausit.zenite.zenmei.bff.controller;
import br.inf.softhausit.zenite.zenmei.bff.client.NotaFiscalServiceClient;
import br.inf.softhausit.zenite.zenmei.model.NotaFiscal;
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
class NotaFiscalBffControllerTest {
    @Mock
    private NotaFiscalServiceClient serviceClient;
    @InjectMocks
    private NotaFiscalBffController controller;
    private NotaFiscal entity;
    @BeforeEach
    void setUp() {
        entity = new NotaFiscal();
        entity.setId("1");
    }
    @Test
    void getAll_ShouldReturnList() {
        List<NotaFiscal> list = Arrays.asList(entity);
        when(serviceClient.getAllNotaFiscals(anyString())).thenReturn(ResponseEntity.ok(list));
        ResponseEntity<List<NotaFiscal>> response = controller.getAllNotaFiscals("user-123");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
    }
    @Test
    void getById_ShouldReturnNotaFiscal() {
        when(serviceClient.getNotaFiscalById(anyString(), anyString())).thenReturn(ResponseEntity.ok(entity));
        ResponseEntity<NotaFiscal> response = controller.getNotaFiscalById("1", "user-123");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }
    @Test
    void create_ShouldReturnCreated() {
        when(serviceClient.createNotaFiscal(any(NotaFiscal.class), anyString()))
            .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(entity));
        ResponseEntity<NotaFiscal> response = controller.createNotaFiscal(entity, "user-123");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
    @Test
    void update_ShouldReturnOk() {
        when(serviceClient.updateNotaFiscal(anyString(), any(NotaFiscal.class), anyString()))
            .thenReturn(ResponseEntity.ok(entity));
        ResponseEntity<NotaFiscal> response = controller.updateNotaFiscal("1", entity, "user-123");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    void delete_ShouldReturnNoContent() {
        when(serviceClient.deleteNotaFiscal(anyString(), anyString()))
            .thenReturn(ResponseEntity.noContent().build());
        ResponseEntity<Void> response = controller.deleteNotaFiscal("1", "user-123");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
