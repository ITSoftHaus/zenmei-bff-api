# ZenMEI BFF API

Backend for Frontend (BFF) - Camada de integração para todos os microsserviços do ZenMEI

## 🎯 Propósito

O ZenMEI BFF atua como uma camada de agregação e orquestração entre o frontend e os microsserviços backend. Ele implementa padrões de resiliência como Circuit Breaker, Retry e Timeout para garantir alta disponibilidade.

## 🏗️ Arquitetura

```
Frontend → BFF API → Microsserviços (User, Client, Serviço, Produto, CNAE, etc.)
```

### Microsserviços Integrados

| Microsserviço | Porta | Endpoint Base | Descrição |
|--------------|-------|---------------|-----------|
| User API | 8081 | `/api/v1/users` | Gerenciamento de usuários |
| Agenda API | 8082 | `/api/v1/compromissos` | Agendamentos e compromissos |
| Chamado API | 8084 | `/api/v1/chamados` | Sistema de chamados |
| Client API | 8085 | `/api/v1/clients` | Gerenciamento de clientes |
| CNAE API | 8086 | `/api/v1/cnaes` | CNAEs e LC116 |
| Despesa API | 8087 | `/api/v1/despesas` | Controle de despesas |
| Nota API | 8088 | `/api/v1/notas` | Emissão de notas fiscais |
| Receita API | 8089 | `/api/v1/vendas` | Vendas e receitas |
| Serviço API | 8090 | `/api/v1/services` | Catálogo de serviços |
| Produto API | 8091 | `/api/v1/produtos` | Catálogo de produtos |

## 🚀 Tecnologias

- **Java 21**
- **Spring Boot 3.5.9**
- **Spring Cloud OpenFeign** - Cliente HTTP declarativo
- **Resilience4j** - Circuit Breaker, Retry, Timeout
- **SpringDoc OpenAPI** - Documentação Swagger
- **Micrometer + Prometheus** - Métricas
- **Lombok** - Redução de boilerplate

## 📋 Pré-requisitos

- Java 21+
- Maven 3.6+
- Microsserviços ZenMEI em execução

## 🔧 Configuração

### Variáveis de Ambiente

Você pode configurar as URLs dos microsserviços através de variáveis de ambiente:

```bash
export USER_API_URL=http://localhost:8081
export AGENDA_API_URL=http://localhost:8082
export CHAMADO_API_URL=http://localhost:8084
export CLIENT_API_URL=http://localhost:8085
export CNAE_API_URL=http://localhost:8086
export DESPESA_API_URL=http://localhost:8087
export NOTA_API_URL=http://localhost:8088
export RECEITA_API_URL=http://localhost:8089
export SERVICO_API_URL=http://localhost:8090
export PRODUTO_API_URL=http://localhost:8091
```

### Executar Localmente

```bash
# Compilar o projeto
./mvnw clean install

# Executar a aplicação
./mvnw spring-boot:run

# Ou executar o JAR
java -jar target/zenmei-bff-api-0.0.1-SNAPSHOT.jar
```

A aplicação estará disponível em: `http://localhost:8080`

## 📚 Documentação da API

Após iniciar a aplicação, acesse:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

## 🔌 Endpoints BFF

Todos os endpoints BFF seguem o padrão: `/api/bff/v1/{recurso}`

### Users
- `GET /api/bff/v1/users` - Listar usuários
- `GET /api/bff/v1/users/{id}` - Buscar usuário
- `POST /api/bff/v1/users` - Criar usuário
- `PUT /api/bff/v1/users/{id}` - Atualizar usuário
- `DELETE /api/bff/v1/users/{id}` - Deletar usuário

### Clients
- `GET /api/bff/v1/clients` - Listar clientes
- `GET /api/bff/v1/clients/{id}` - Buscar cliente
- `POST /api/bff/v1/clients` - Criar cliente
- `PUT /api/bff/v1/clients/{id}` - Atualizar cliente
- `DELETE /api/bff/v1/clients/{id}` - Deletar cliente

### Services
- `GET /api/bff/v1/services` - Listar serviços
- `GET /api/bff/v1/services/{id}` - Buscar serviço
- `POST /api/bff/v1/services` - Criar serviço
- `PUT /api/bff/v1/services` - Atualizar serviço
- `DELETE /api/bff/v1/services/{id}` - Deletar serviço

### Produtos
- `GET /api/bff/v1/produtos` - Listar produtos
- `GET /api/bff/v1/produtos/{id}` - Buscar produto
- `POST /api/bff/v1/produtos` - Criar produto
- `PUT /api/bff/v1/produtos/{id}` - Atualizar produto
- `DELETE /api/bff/v1/produtos/{id}` - Deletar produto

### CNAEs
- `GET /api/bff/v1/cnaes` - Listar CNAEs MEI
- `GET /api/bff/v1/cnaes/lc116` - Listar LC116
- `GET /api/bff/v1/cnaes/lc116/{codigo}` - Buscar LC116
- `GET /api/bff/v1/cnaes/lista` - Listar todos os CNAEs
- `GET /api/bff/v1/cnaes/consulta/tipo/{codigo}` - Buscar CNAE

## 🛡️ Resiliência

### Circuit Breaker

O BFF implementa Circuit Breaker em todas as chamadas aos microsserviços:

- **Janela deslizante**: 10 chamadas
- **Taxa de falha**: 50%
- **Tempo de espera (open state)**: 5 segundos
- **Chamadas permitidas (half-open)**: 3

### Retry

Configuração de retry automático:

- **Tentativas máximas**: 3
- **Tempo de espera**: 1 segundo
- **Backoff exponencial**: Multiplicador de 2

### Timeout

- **Timeout de conexão**: 5 segundos
- **Timeout de leitura**: 5 segundos

## 📊 Monitoramento

### Actuator Endpoints

- **Health**: http://localhost:8080/actuator/health
- **Metrics**: http://localhost:8080/actuator/metrics
- **Prometheus**: http://localhost:8080/actuator/prometheus
- **Circuit Breakers**: http://localhost:8080/actuator/circuitbreakers

### Métricas Prometheus

O BFF expõe métricas no formato Prometheus, incluindo:
- Contadores de requisições por endpoint
- Tempos de resposta
- Status dos Circuit Breakers
- Taxa de erro por serviço

## 🔐 Headers

O BFF propaga automaticamente os seguintes headers:

- `Authorization` - Token JWT
- `X-User-Id` - Identificador do usuário
- `X-Request-Id` - ID para rastreamento distribuído
- `Content-Type` - Tipo de conteúdo

## 🐳 Docker

```bash
# Build da imagem
docker build -t zenmei-bff-api .

# Executar container
docker run -p 8080:8080 \
  -e USER_API_URL=http://host.docker.internal:8081 \
  -e CLIENT_API_URL=http://host.docker.internal:8085 \
  zenmei-bff-api
```

## 🧪 Testes

```bash
# Executar testes
./mvnw test

# Executar testes com coverage
./mvnw test jacoco:report
```

## 📝 Logs

Os logs seguem um padrão estruturado:

```
HH:mm:ss.SSS --- [thread-name] logger-name : message
```

Níveis de log:
- **DEBUG**: Chamadas aos microsserviços
- **INFO**: Requisições ao BFF
- **ERROR**: Falhas e fallbacks

## 🤝 Contribuindo

1. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
2. Commit suas mudanças (`git commit -m 'Adiciona MinhaFeature'`)
3. Push para a branch (`git push origin feature/MinhaFeature`)
4. Abra um Pull Request

## 📄 Licença

Este projeto é parte do sistema ZenMEI.

## 👥 Equipe

ZenMEI Development Team - [SoftHaus IT](https://softhausit.com.br)

## 🔗 Links Úteis

- [Spring Cloud OpenFeign](https://spring.io/projects/spring-cloud-openfeign)
- [Resilience4j](https://resilience4j.readme.io/)
- [SpringDoc OpenAPI](https://springdoc.org/)
