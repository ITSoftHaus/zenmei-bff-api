package br.inf.softhausit.zenite.zenmei.bff.controller;
import br.inf.softhausit.zenite.zenmei.bff.client.AgendaServiceClient;
import br.inf.softhausit.zenite.zenmei.model.Agenda;
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
class AgendaBffControllerTest {
    @Mock
    private AgendaServiceClient serviceClient;
    @InjectMocks
    private AgendaBffController controller;
    private Agenda entity;
    @BeforeEach
    void setUp() {
        entity = new Agenda();
        entity.setId("1");
    }
    @Test
    void getAll_ShouldReturnList() {
        List<Agenda> list = Arrays.asList(entity);
        when(serviceClient.getAllAgendas(anyString())).thenReturn(ResponseEntity.ok(list));
        ResponseEntity<List<Agenda>> response = controller.getAllAgendas("user-123");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
    }
    @Test
    void getById_ShouldReturnAgenda() {
        when(serviceClient.getAgendaById(anyString(), anyString())).thenReturn(ResponseEntity.ok(entity));
        ResponseEntity<Agenda> response = controller.getAgendaById("1", "user-123");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }
    @Test
    void create_ShouldReturnCreated() {
        when(serviceClient.createAgenda(any(Agenda.class), anyString()))
            .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(entity));
        ResponseEntity<Agenda> response = controller.createAgenda(entity, "user-123");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
    @Test
    void update_ShouldReturnOk() {
        when(serviceClient.updateAgenda(anyString(), any(Agenda.class), anyString()))
            .thenReturn(ResponseEntity.ok(entity));
        ResponseEntity<Agenda> response = controller.updateAgenda("1", entity, "user-123");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    void delete_ShouldReturnNoContent() {
        when(serviceClient.deleteAgenda(anyString(), anyString()))
            .thenReturn(ResponseEntity.noContent().build());
        ResponseEntity<Void> response = controller.deleteAgenda("1", "user-123");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
