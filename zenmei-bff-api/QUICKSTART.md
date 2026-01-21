# Guia de Início Rápido - ZenMEI BFF

## 🚀 Setup Inicial

### 1. Pré-requisitos

```bash
# Verificar Java
java -version  # Deve ser 21+

# Verificar Maven
./mvnw -version
```

### 2. Configurar Variáveis de Ambiente (Opcional)

Crie um arquivo `.env` na raiz do projeto:

```bash
# URLs dos Microsserviços
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

Carregar variáveis:
```bash
source .env
```

### 3. Compilar e Executar

```bash
# Compilar
./mvnw clean install -DskipTests

# Executar
./mvnw spring-boot:run

# Ou executar o JAR
java -jar target/zenmei-bff-api-0.0.1-SNAPSHOT.jar
```

## 🧪 Testar a Aplicação

### 1. Health Check

```bash
# Verificar se o BFF está rodando
curl http://localhost:8080/actuator/health

# Resposta esperada
{
  "status": "UP"
}
```

### 2. Informações do BFF

```bash
curl http://localhost:8080/api/bff/v1/info
```

### 3. Ping

```bash
curl http://localhost:8080/api/bff/v1/ping
```

## 📖 Documentação da API

Acesse o Swagger UI:
```
http://localhost:8080/swagger-ui.html
```

## 🔌 Testando Endpoints

### Exemplo: Listar Usuários

```bash
curl -X GET http://localhost:8080/api/bff/v1/users \
  -H "X-User-Id: 550e8400-e29b-41d4-a716-446655440000" \
  -H "Authorization: Bearer seu-token-jwt"
```

### Exemplo: Listar Clientes

```bash
curl -X GET http://localhost:8080/api/bff/v1/clients \
  -H "X-User-Id: 550e8400-e29b-41d4-a716-446655440000"
```

### Exemplo: Criar Serviço

```bash
curl -X POST http://localhost:8080/api/bff/v1/services \
  -H "Content-Type: application/json" \
  -H "X-User-Id: 550e8400-e29b-41d4-a716-446655440000" \
  -d '{
    "nome": "Consultoria em TI",
    "descricao": "Serviços de consultoria",
    "valor": 150.00
  }'
```

## 🐳 Docker

### Build da Imagem

```bash
docker build -t zenmei-bff-api:latest .
```

### Executar Container

```bash
docker run -d \
  -p 8080:8080 \
  -e USER_API_URL=http://host.docker.internal:8081 \
  -e CLIENT_API_URL=http://host.docker.internal:8085 \
  -e SERVICO_API_URL=http://host.docker.internal:8090 \
  -e PRODUTO_API_URL=http://host.docker.internal:8091 \
  -e CNAE_API_URL=http://host.docker.internal:8086 \
  --name zenmei-bff \
  zenmei-bff-api:latest
```

### Ver Logs

```bash
docker logs -f zenmei-bff
```

## 📊 Monitoramento

### Métricas Prometheus

```bash
curl http://localhost:8080/actuator/prometheus
```

### Estado dos Circuit Breakers

```bash
curl http://localhost:8080/actuator/circuitbreakers
```

### Métricas Específicas

```bash
# Listar todas as métricas
curl http://localhost:8080/actuator/metrics

# Métrica específica
curl http://localhost:8080/actuator/metrics/http.server.requests
```

## 🔧 Configuração por Perfil

### Desenvolvimento (padrão)

```bash
./mvnw spring-boot:run
```

### Produção

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

### Custom Profile

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=staging
```

## 🧰 Troubleshooting

### Problema: Circuit Breaker abrindo constantemente

**Solução**: Verificar se os microsserviços estão rodando

```bash
# Verificar User API
curl http://localhost:8081/actuator/health

# Verificar Client API
curl http://localhost:8085/actuator/health
```

### Problema: Timeout nas requisições

**Solução**: Aumentar os timeouts no `application.yml`

```yaml
feign:
  client:
    config:
      default:
        connectTimeout: 10000  # 10 segundos
        readTimeout: 10000
```

### Problema: Erro 503 Service Unavailable

**Causa**: Circuit breaker aberto ou serviço indisponível

**Solução**: 
1. Verificar logs do BFF
2. Verificar health do microsserviço
3. Aguardar o circuit breaker fechar (5 segundos)

```bash
# Ver estado do circuit breaker
curl http://localhost:8080/actuator/circuitbreakers
```

## 📝 Logs

### Ver Logs em Tempo Real

```bash
# Durante execução com Maven
./mvnw spring-boot:run

# Docker
docker logs -f zenmei-bff
```

### Logs Estruturados

```
14:30:45.123 --- [nio-8080-exec-1] UserBffController : BFF: Listando usuários
14:30:45.234 --- [nio-8080-exec-1] UserService : Listando usuários para userId: 550e8400...
14:30:45.456 --- [nio-8080-exec-1] UserService : Resposta recebida do user-service
```

## 🎯 Próximos Passos

1. ✅ Verificar que todos os microsserviços estão rodando
2. ✅ Testar cada endpoint do BFF
3. ✅ Configurar monitoramento (Prometheus + Grafana)
4. ✅ Implementar testes de integração
5. ✅ Configurar CI/CD

## 📚 Recursos Adicionais

- [README.md](README.md) - Documentação completa
- [ARCHITECTURE.md](ARCHITECTURE.md) - Arquitetura detalhada
- [Swagger UI](http://localhost:8080/swagger-ui.html) - Documentação interativa

## 🆘 Suporte

Para problemas ou dúvidas:
- Email: dev@softhausit.com.br
- Documentação: `/docs`
- Issues: GitHub Issues
