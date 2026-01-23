package br.inf.softhausit.zenite.zenmei.bff.controller;
import br.inf.softhausit.zenite.zenmei.bff.client.ChamadoServiceClient;
import br.inf.softhausit.zenite.zenmei.model.Chamado;
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
class ChamadoBffControllerTest {
    @Mock
    private ChamadoServiceClient serviceClient;
    @InjectMocks
    private ChamadoBffController controller;
    private Chamado entity;
    @BeforeEach
    void setUp() {
        entity = new Chamado();
        entity.setId("1");
    }
    @Test
    void getAll_ShouldReturnList() {
        List<Chamado> list = Arrays.asList(entity);
        when(serviceClient.getAllChamados(anyString())).thenReturn(ResponseEntity.ok(list));
        ResponseEntity<List<Chamado>> response = controller.getAllChamados("user-123");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
    }
    @Test
    void getById_ShouldReturnChamado() {
        when(serviceClient.getChamadoById(anyString(), anyString())).thenReturn(ResponseEntity.ok(entity));
        ResponseEntity<Chamado> response = controller.getChamadoById("1", "user-123");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }
    @Test
    void create_ShouldReturnCreated() {
        when(serviceClient.createChamado(any(Chamado.class), anyString()))
            .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(entity));
        ResponseEntity<Chamado> response = controller.createChamado(entity, "user-123");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
    @Test
    void update_ShouldReturnOk() {
        when(serviceClient.updateChamado(anyString(), any(Chamado.class), anyString()))
            .thenReturn(ResponseEntity.ok(entity));
        ResponseEntity<Chamado> response = controller.updateChamado("1", entity, "user-123");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    void delete_ShouldReturnNoContent() {
        when(serviceClient.deleteChamado(anyString(), anyString()))
            .thenReturn(ResponseEntity.noContent().build());
        ResponseEntity<Void> response = controller.deleteChamado("1", "user-123");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
