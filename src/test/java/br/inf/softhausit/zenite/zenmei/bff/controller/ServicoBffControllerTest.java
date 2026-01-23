package br.inf.softhausit.zenite.zenmei.bff.controller;
import br.inf.softhausit.zenite.zenmei.bff.client.ServicoServiceClient;
import br.inf.softhausit.zenite.zenmei.model.Servico;
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
class ServicoBffControllerTest {
    @Mock
    private ServicoServiceClient serviceClient;
    @InjectMocks
    private ServicoBffController controller;
    private Servico entity;
    @BeforeEach
    void setUp() {
        entity = new Servico();
        entity.setId("1");
    }
    @Test
    void getAll_ShouldReturnList() {
        List<Servico> list = Arrays.asList(entity);
        when(serviceClient.getAllServicos(anyString())).thenReturn(ResponseEntity.ok(list));
        ResponseEntity<List<Servico>> response = controller.getAllServicos("user-123");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
    }
    @Test
    void getById_ShouldReturnServico() {
        when(serviceClient.getServicoById(anyString(), anyString())).thenReturn(ResponseEntity.ok(entity));
        ResponseEntity<Servico> response = controller.getServicoById("1", "user-123");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }
    @Test
    void create_ShouldReturnCreated() {
        when(serviceClient.createServico(any(Servico.class), anyString()))
            .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(entity));
        ResponseEntity<Servico> response = controller.createServico(entity, "user-123");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
    @Test
    void update_ShouldReturnOk() {
        when(serviceClient.updateServico(anyString(), any(Servico.class), anyString()))
            .thenReturn(ResponseEntity.ok(entity));
        ResponseEntity<Servico> response = controller.updateServico("1", entity, "user-123");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    void delete_ShouldReturnNoContent() {
        when(serviceClient.deleteServico(anyString(), anyString()))
            .thenReturn(ResponseEntity.noContent().build());
        ResponseEntity<Void> response = controller.deleteServico("1", "user-123");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
