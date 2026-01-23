package br.inf.softhausit.zenite.zenmei.bff.controller;
import br.inf.softhausit.zenite.zenmei.bff.client.ProdutoServiceClient;
import br.inf.softhausit.zenite.zenmei.model.Produto;
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
class ProdutoBffControllerTest {
    @Mock
    private ProdutoServiceClient serviceClient;
    @InjectMocks
    private ProdutoBffController controller;
    private Produto entity;
    @BeforeEach
    void setUp() {
        entity = new Produto();
        entity.setId("1");
    }
    @Test
    void getAll_ShouldReturnList() {
        List<Produto> list = Arrays.asList(entity);
        when(serviceClient.getAllProdutos(anyString())).thenReturn(ResponseEntity.ok(list));
        ResponseEntity<List<Produto>> response = controller.getAllProdutos("user-123");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
    }
    @Test
    void getById_ShouldReturnProduto() {
        when(serviceClient.getProdutoById(anyString(), anyString())).thenReturn(ResponseEntity.ok(entity));
        ResponseEntity<Produto> response = controller.getProdutoById("1", "user-123");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }
    @Test
    void create_ShouldReturnCreated() {
        when(serviceClient.createProduto(any(Produto.class), anyString()))
            .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(entity));
        ResponseEntity<Produto> response = controller.createProduto(entity, "user-123");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
    @Test
    void update_ShouldReturnOk() {
        when(serviceClient.updateProduto(anyString(), any(Produto.class), anyString()))
            .thenReturn(ResponseEntity.ok(entity));
        ResponseEntity<Produto> response = controller.updateProduto("1", entity, "user-123");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    void delete_ShouldReturnNoContent() {
        when(serviceClient.deleteProduto(anyString(), anyString()))
            .thenReturn(ResponseEntity.noContent().build());
        ResponseEntity<Void> response = controller.deleteProduto("1", "user-123");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
