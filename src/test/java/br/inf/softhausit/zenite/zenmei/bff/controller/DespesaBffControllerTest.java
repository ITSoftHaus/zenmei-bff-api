package br.inf.softhausit.zenite.zenmei.bff.controller;
import br.inf.softhausit.zenite.zenmei.bff.client.DespesaServiceClient;
import br.inf.softhausit.zenite.zenmei.model.Despesa;
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
class DespesaBffControllerTest {
    @Mock
    private DespesaServiceClient serviceClient;
    @InjectMocks
    private DespesaBffController controller;
    private Despesa entity;
    @BeforeEach
    void setUp() {
        entity = new Despesa();
        entity.setId("1");
    }
    @Test
    void getAll_ShouldReturnList() {
        List<Despesa> list = Arrays.asList(entity);
        when(serviceClient.getAllDespesas(anyString())).thenReturn(ResponseEntity.ok(list));
        ResponseEntity<List<Despesa>> response = controller.getAllDespesas("user-123");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
    }
    @Test
    void getById_ShouldReturnDespesa() {
        when(serviceClient.getDespesaById(anyString(), anyString())).thenReturn(ResponseEntity.ok(entity));
        ResponseEntity<Despesa> response = controller.getDespesaById("1", "user-123");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }
    @Test
    void create_ShouldReturnCreated() {
        when(serviceClient.createDespesa(any(Despesa.class), anyString()))
            .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(entity));
        ResponseEntity<Despesa> response = controller.createDespesa(entity, "user-123");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
    @Test
    void update_ShouldReturnOk() {
        when(serviceClient.updateDespesa(anyString(), any(Despesa.class), anyString()))
            .thenReturn(ResponseEntity.ok(entity));
        ResponseEntity<Despesa> response = controller.updateDespesa("1", entity, "user-123");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    void delete_ShouldReturnNoContent() {
        when(serviceClient.deleteDespesa(anyString(), anyString()))
            .thenReturn(ResponseEntity.noContent().build());
        ResponseEntity<Void> response = controller.deleteDespesa("1", "user-123");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
