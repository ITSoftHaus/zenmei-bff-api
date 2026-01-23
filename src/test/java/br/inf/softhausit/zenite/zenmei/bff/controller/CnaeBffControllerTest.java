package br.inf.softhausit.zenite.zenmei.bff.controller;
import br.inf.softhausit.zenite.zenmei.bff.client.CnaeServiceClient;
import br.inf.softhausit.zenite.zenmei.model.Cnae;
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
class CnaeBffControllerTest {
    @Mock
    private CnaeServiceClient serviceClient;
    @InjectMocks
    private CnaeBffController controller;
    private Cnae entity;
    @BeforeEach
    void setUp() {
        entity = new Cnae();
        entity.setId("1");
    }
    @Test
    void getAll_ShouldReturnList() {
        List<Cnae> list = Arrays.asList(entity);
        when(serviceClient.getAllCnaes(anyString())).thenReturn(ResponseEntity.ok(list));
        ResponseEntity<List<Cnae>> response = controller.getAllCnaes("user-123");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
    }
    @Test
    void getById_ShouldReturnCnae() {
        when(serviceClient.getCnaeById(anyString(), anyString())).thenReturn(ResponseEntity.ok(entity));
        ResponseEntity<Cnae> response = controller.getCnaeById("1", "user-123");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }
    @Test
    void create_ShouldReturnCreated() {
        when(serviceClient.createCnae(any(Cnae.class), anyString()))
            .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(entity));
        ResponseEntity<Cnae> response = controller.createCnae(entity, "user-123");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
    @Test
    void update_ShouldReturnOk() {
        when(serviceClient.updateCnae(anyString(), any(Cnae.class), anyString()))
            .thenReturn(ResponseEntity.ok(entity));
        ResponseEntity<Cnae> response = controller.updateCnae("1", entity, "user-123");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    void delete_ShouldReturnNoContent() {
        when(serviceClient.deleteCnae(anyString(), anyString()))
            .thenReturn(ResponseEntity.noContent().build());
        ResponseEntity<Void> response = controller.deleteCnae("1", "user-123");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
