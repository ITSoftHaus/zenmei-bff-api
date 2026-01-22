# Arquitetura do ZenMEI BFF

## Visão Geral

O ZenMEI BFF (Backend for Frontend) é uma camada de agregação que atua como ponto único de entrada para o frontend, integrando todos os microsserviços do ecossistema ZenMEI.

## Arquitetura em Camadas

```
┌─────────────────────────────────────────────┐
│           Frontend Application              │
└────────────────┬────────────────────────────┘
                 │ HTTP/REST
                 ▼
┌─────────────────────────────────────────────┐
│             ZenMEI BFF API                  │
│  ┌─────────────────────────────────────┐   │
│  │        Controllers Layer            │   │
│  │  - UserBffController                │   │
│  │  - ClientBffController              │   │
│  │  - ServicoBffController             │   │
│  │  - ProdutoBffController             │   │
│  │  - CnaeBffController                │   │
│  │  - ... (outros controllers)         │   │
│  └────────────┬────────────────────────┘   │
│               │                             │
│  ┌────────────▼────────────────────────┐   │
│  │        Service Layer                │   │
│  │  - @CircuitBreaker                  │   │
│  │  - @Retry                           │   │
│  │  - Fallback Methods                 │   │
│  │  - Business Logic                   │   │
│  └────────────┬────────────────────────┘   │
│               │                             │
│  ┌────────────▼────────────────────────┐   │
│  │     Feign Clients Layer            │   │
│  │  - UserClient                       │   │
│  │  - ClientClient                     │   │
│  │  - ServicoClient                    │   │
│  │  - ... (outros clients)             │   │
│  └────────────┬────────────────────────┘   │
│               │                             │
│  ┌────────────▼────────────────────────┐   │
│  │   Configuration & Interceptors      │   │
│  │  - FeignConfig                      │   │
│  │  - RequestInterceptor               │   │
│  │  - ErrorDecoder                     │   │
│  │  - GlobalExceptionHandler           │   │
│  └─────────────────────────────────────┘   │
└────────────────┬────────────────────────────┘
                 │ HTTP/REST + Resilience
                 ▼
┌─────────────────────────────────────────────┐
│          Microsserviços Backend             │
│  ┌──────┐ ┌──────┐ ┌──────┐ ┌──────┐      │
│  │ User │ │Client│ │Serviço││Produto│ ...  │
│  │ 8081 │ │ 8085 │ │ 8090 │ │ 8091 │      │
│  └──────┘ └──────┘ └──────┘ └──────┘      │
└─────────────────────────────────────────────┘
```

## Componentes Principais

### 1. Controllers (API Layer)

Responsáveis por expor os endpoints REST do BFF:

- **UserBffController**: `/api/bff/v1/users`
- **ClientBffController**: `/api/bff/v1/clients`
- **ServicoBffController**: `/api/bff/v1/services`
- **ProdutoBffController**: `/api/bff/v1/produtos`
- **CnaeBffController**: `/api/bff/v1/cnaes`

**Responsabilidades:**
- Validação de entrada
- Logging de requisições
- Documentação OpenAPI
- Roteamento para camada de serviço

### 2. Service Layer

Implementa a lógica de negócio e padrões de resiliência:

```java
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserClient userClient;
    
    @CircuitBreaker(name = "userService", fallbackMethod = "fallback")
    @Retry(name = "userService")
    public ResponseEntity<?> listarUsuarios(UUID userId) {
        return userClient.listarUsuarios(userId);
    }
    
    private ResponseEntity<?> fallback(UUID userId, Exception e) {
        log.error("Fallback ativado", e);
        return ResponseEntity.ok(Collections.emptyList());
    }
}
```

**Responsabilidades:**
- Orquestração de chamadas
- Implementação de Circuit Breaker
- Implementação de Retry
- Métodos de fallback
- Agregação de dados (quando necessário)

### 3. Feign Clients

Clientes HTTP declarativos para comunicação com microsserviços:

```java
@FeignClient(
    name = "user-service",
    url = "${microservices.user-api.url}",
    configuration = FeignConfig.class
)
public interface UserClient {
    @GetMapping("/api/v1/users")
    ResponseEntity<?> listarUsuarios(@RequestHeader("X-User-Id") UUID userId);
}
```

**Responsabilidades:**
- Definição de contratos de API
- Serialização/Deserialização
- Tratamento de erros HTTP

### 4. Configuration

#### FeignConfig
- Configuração de interceptadores
- Propagação de headers
- Decodificação de erros

#### OpenApiConfig
- Documentação Swagger
- Metadados da API
- Configuração de servidores

### 5. Exception Handling

#### GlobalExceptionHandler
Tratamento centralizado de exceções:

- `MicroserviceException`: Erros dos microsserviços
- `FeignException`: Erros de comunicação
- `CallNotPermittedException`: Circuit breaker aberto
- `TimeoutException`: Timeout de requisição
- `MethodArgumentNotValidException`: Validação

## Padrões de Resiliência

### Circuit Breaker

```yaml
resilience4j:
  circuitbreaker:
    configs:
      default:
        slidingWindowSize: 10        # Janela de 10 chamadas
        failureRateThreshold: 50     # 50% de falha abre o circuit
        waitDurationInOpenState: 5s  # Aguarda 5s antes de half-open
```

**Estados:**
1. **CLOSED**: Funcionamento normal
2. **OPEN**: Serviço indisponível, retorna fallback
3. **HALF_OPEN**: Teste de recuperação

### Retry Pattern

```yaml
resilience4j:
  retry:
    configs:
      default:
        maxAttempts: 3                      # 3 tentativas
        waitDuration: 1s                    # Aguarda 1s
        enableExponentialBackoff: true      # Backoff exponencial
        exponentialBackoffMultiplier: 2     # Multiplica por 2
```

**Tentativas:**
- 1ª tentativa: imediata
- 2ª tentativa: após 1s
- 3ª tentativa: após 2s

### Timeout

```yaml
feign:
  client:
    config:
      default:
        connectTimeout: 5000  # 5 segundos para conectar
        readTimeout: 5000     # 5 segundos para ler resposta
```

## Fluxo de Dados

### Requisição Normal

```
1. Frontend → BFF Controller
2. Controller → Service Layer
3. Service Layer → Feign Client
4. Feign Client → Microsserviço
5. Microsserviço → Response
6. Response → BFF → Frontend
```

### Requisição com Falha e Retry

```
1. Frontend → BFF Controller
2. Controller → Service Layer
3. Service Layer → Feign Client
4. Feign Client → Microsserviço (FALHA)
5. Retry 1 → Aguarda 1s → Microsserviço (FALHA)
6. Retry 2 → Aguarda 2s → Microsserviço (SUCESSO)
7. Response → BFF → Frontend
```

### Requisição com Circuit Breaker Aberto

```
1. Frontend → BFF Controller
2. Controller → Service Layer
3. Service Layer → Circuit Breaker (OPEN)
4. Fallback Method → Response Default
5. Response → BFF → Frontend
```

## Propagação de Headers

O BFF propaga automaticamente headers importantes:

```java
@Bean
public RequestInterceptor requestInterceptor() {
    return requestTemplate -> {
        // Authorization (JWT)
        String auth = request.getHeader("Authorization");
        requestTemplate.header("Authorization", auth);
        
        // User ID
        String userId = request.getHeader("X-User-Id");
        requestTemplate.header("X-User-Id", userId);
        
        // Request ID (Distributed Tracing)
        String requestId = request.getHeader("X-Request-Id");
        requestTemplate.header("X-Request-Id", requestId);
    };
}
```

## Observabilidade

### Métricas (Prometheus)

```
# Contadores de requisições
http_server_requests_seconds_count{uri="/api/bff/v1/users"}

# Tempos de resposta
http_server_requests_seconds_sum{uri="/api/bff/v1/users"}

# Estado do Circuit Breaker
resilience4j_circuitbreaker_state{name="userService",state="closed"}

# Taxa de falha
resilience4j_circuitbreaker_failure_rate{name="userService"}
```

### Logs

```
# Padrão de log
HH:mm:ss.SSS --- [thread] logger : message

# Exemplo
14:30:45.123 --- [nio-8080-exec-1] UserBffController : BFF: Listando usuários
14:30:45.234 --- [nio-8080-exec-1] UserService : Chamando user-service
14:30:45.456 --- [nio-8080-exec-1] UserService : Resposta recebida: 200 OK
```

### Health Checks

```bash
# Health geral
GET /actuator/health

# Estado dos Circuit Breakers
GET /actuator/circuitbreakers

# Métricas detalhadas
GET /actuator/metrics
```

## Segurança

### Headers de Autenticação

```http
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
X-User-Id: 550e8400-e29b-41d4-a716-446655440000
```

### CORS (Configuração futura)

```java
@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/bff/**")
                    .allowedOrigins("http://localhost:3000")
                    .allowedMethods("GET", "POST", "PUT", "DELETE");
            }
        };
    }
}
```

## Configuração de Ambiente

### Desenvolvimento

```yaml
server:
  port: 8080

microservices:
  user-api:
    url: http://localhost:8081
  # ... outros serviços
```

### Produção

```yaml
server:
  port: ${PORT:8080}

microservices:
  user-api:
    url: ${USER_API_URL}
  # ... outros serviços via variáveis de ambiente
```

## Escalabilidade

### Horizontal Scaling

O BFF é stateless e pode ser escalado horizontalmente:

```bash
# Kubernetes Deployment
apiVersion: apps/v1
kind: Deployment
metadata:
  name: zenmei-bff
spec:
  replicas: 3  # 3 instâncias
  selector:
    matchLabels:
      app: zenmei-bff
  template:
    spec:
      containers:
      - name: zenmei-bff
        image: zenmei-bff:latest
        ports:
        - containerPort: 8080
```

### Load Balancing

```
┌─────────┐
│  Nginx  │
│   LB    │
└────┬────┘
     │
     ├───► BFF Instance 1
     ├───► BFF Instance 2
     └───► BFF Instance 3
```

## Boas Práticas Implementadas

1. ✅ **Separation of Concerns**: Camadas bem definidas
2. ✅ **Resilience Patterns**: Circuit Breaker, Retry, Timeout
3. ✅ **Fail Fast**: Timeouts adequados
4. ✅ **Graceful Degradation**: Fallback methods
5. ✅ **Observability**: Logs, métricas, health checks
6. ✅ **API Documentation**: OpenAPI/Swagger
7. ✅ **Error Handling**: Tratamento centralizado
8. ✅ **Header Propagation**: Contexto distribuído
9. ✅ **Stateless Design**: Escalabilidade horizontal
10. ✅ **Configuration Management**: Externalização de configs

## Próximos Passos

- [ ] Implementar cache Redis para respostas frequentes
- [ ] Adicionar rate limiting por usuário
- [ ] Implementar autenticação JWT no BFF
- [ ] Adicionar tracing distribuído (Zipkin/Jaeger)
- [ ] Implementar agregação de dados cross-service
- [ ] Adicionar GraphQL como alternativa ao REST
- [ ] Configurar CORS para produção
- [ ] Implementar WebSocket para notificações real-time
