# 🚀 ZenMei - Sistema Completo para Microempreendedores Individuais

[![CI/CD](https://github.com/ITSoftHaus/zenmei/actions/workflows/ci-cd.yml/badge.svg)](https://github.com/ITSoftHaus/zenmei/actions)
[![codecov](https://codecov.io/gh/ITSoftHaus/zenmei/branch/main/graph/badge.svg)](https://codecov.io/gh/ITSoftHaus/zenmei)
[![SonarQube](https://sonarcloud.io/api/project_badges/measure?project=zenmei&metric=alert_status)](https://sonarcloud.io/dashboard?id=zenmei)

Sistema completo de gestão para Microempreendedores Individuais (MEI) com arquitetura de microsserviços, Firebase Authentication e frontend React.

---

## 📊 Status do Projeto

- ✅ **11 Microsserviços** implementados
- ✅ **85%+ Test Coverage** (408 testes)
- ✅ **Firebase Security** em todos os serviços
- ✅ **SonarQube** configurado
- ✅ **Docker** ready
- ✅ **CI/CD** automatizado

---

## 🏗️ Arquitetura

```
┌─────────────────────────────────────────────────────────┐
│                     Frontend (React)                    │
│                  http://localhost:5173                  │
└──────────────────────┬──────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────┐
│              BFF API (Backend for Frontend)             │
│                  http://localhost:8091                  │
└──┬──┬──┬──┬──┬──┬──┬──┬──┬──┬────────────────────────────┘
   │  │  │  │  │  │  │  │  │  │
   ▼  ▼  ▼  ▼  ▼  ▼  ▼  ▼  ▼  ▼
┌────────────────────────────────────────────────────────┐
│                  Microsserviços                        │
├────────────────────────────────────────────────────────┤
│  MEI API         │  Client API     │  Agenda API      │
│  :8081           │  :8082          │  :8083           │
├──────────────────┼─────────────────┼──────────────────┤
│  Chamado API     │  CNAE API       │  Despesa API     │
│  :8084           │  :8085          │  :8086           │
├──────────────────┼─────────────────┼──────────────────┤
│  Nota API        │  Produto API    │  Receita API     │
│  :8087           │  :8088          │  :8089           │
├──────────────────┴─────────────────┴──────────────────┤
│               Servico API - :8090                      │
└────────────────────────────────────────────────────────┘
                       │
        ┌──────────────┴──────────────┐
        ▼                             ▼
┌──────────────┐              ┌──────────────┐
│  PostgreSQL  │              │    Redis     │
│    :5432     │              │    :6379     │
└──────────────┘              └──────────────┘
```

---

## 🚀 Quick Start

### Pré-requisitos

- Docker & Docker Compose
- Git
- Node.js 20+ (para desenvolvimento local)
- Java 21+ (para desenvolvimento local)

### 1. Clone o Repositório

```bash
git clone https://github.com/ITSoftHaus/zenmei.git
cd zenmei
```

### 2. Configure Variáveis de Ambiente

```bash
cp .env.example .env
# Edite .env com suas credenciais do Firebase
```

### 3. Inicie Todos os Serviços

```bash
./start.sh
```

Ou manualmente:

```bash
docker-compose up -d
```

### 4. Acesse a Aplicação

- **Frontend:** http://localhost:5173
- **BFF API:** http://localhost:8091
- **Swagger (BFF):** http://localhost:8091/swagger-ui.html

---

## 📦 Microsserviços

| Serviço | Porta | Descrição | URL Health |
|---------|-------|-----------|------------|
| **MEI API** | 8081 | Gestão de MEIs | http://localhost:8081/actuator/health |
| **Client API** | 8082 | Gestão de Clientes | http://localhost:8082/actuator/health |
| **Agenda API** | 8083 | Agendamentos | http://localhost:8083/actuator/health |
| **Chamado API** | 8084 | Chamados/Tickets | http://localhost:8084/actuator/health |
| **CNAE API** | 8085 | Códigos CNAE | http://localhost:8085/actuator/health |
| **Despesa API** | 8086 | Despesas | http://localhost:8086/actuator/health |
| **Nota API** | 8087 | Notas Fiscais | http://localhost:8087/actuator/health |
| **Produto API** | 8088 | Produtos | http://localhost:8088/actuator/health |
| **Receita API** | 8089 | Receitas | http://localhost:8089/actuator/health |
| **Servico API** | 8090 | Serviços | http://localhost:8090/actuator/health |
| **BFF API** | 8091 | Backend for Frontend | http://localhost:8091/actuator/health |

---

## 🛠️ Tecnologias

### Backend
- **Java 21** - Linguagem
- **Spring Boot 3.5.9** - Framework
- **Spring Security** - Segurança
- **Firebase Admin SDK** - Autenticação JWT
- **PostgreSQL** - Banco de dados
- **Redis** - Cache
- **Feign** - HTTP Client (BFF)
- **JUnit 5 + Mockito** - Testes
- **JaCoCo** - Coverage
- **SonarQube** - Qualidade de código

### Frontend
- **React 18** - UI Framework
- **TypeScript** - Linguagem
- **Vite** - Build tool
- **Zustand** - State management
- **React Hook Form + Zod** - Forms & Validation
- **Tailwind CSS** - Styling
- **Firebase SDK** - Autenticação
- **Vitest** - Testes

### DevOps
- **Docker** - Containerização
- **Docker Compose** - Orquestração local
- **GitHub Actions** - CI/CD
- **Google Cloud Run** - Deploy produção
- **SonarCloud** - Análise estática

---

## 🧪 Testes

### Rodar Testes Backend

```bash
# Todos os microsserviços
for api in zenmei-*-api; do
    cd $api && mvn test && cd ..
done

# Um serviço específico
cd zenmei-mei-api
mvn test
```

### Rodar Testes Frontend

```bash
cd zenite-mei-app
npm run test
```

### Coverage Report

```bash
# Backend
cd zenmei-mei-api
mvn test jacoco:report
open target/site/jacoco/index.html

# Frontend
cd zenite-mei-app
npm run test:run -- --coverage
```

---

## 🔐 Segurança

### Firebase Authentication

Todos os microsserviços protegidos com Firebase JWT:

```
Frontend → Firebase Login → JWT Token
          ↓
Backend → FirebaseAuthenticationFilter → Token Validation
          ↓
Spring Security Context → Protected Resources
```

### Configuração

1. Configure Firebase no `.env`:
```bash
FIREBASE_CREDENTIALS_JSON='{"type":"service_account",...}'
```

2. Autenticação no frontend:
```typescript
const token = await user.getIdToken();
axios.get('/api/endpoint', {
  headers: { Authorization: `Bearer ${token}` }
});
```

---

## 📊 Observabilidade

### Health Checks

Todos os serviços expõem:
```
/actuator/health - Status geral
/actuator/health/liveness - Liveness probe
/actuator/health/readiness - Readiness probe
```

### Metrics

Prometheus metrics em:
```
/actuator/prometheus
```

### Logs

Logs estruturados em JSON (logback):
```json
{
  "timestamp": "2026-01-23T15:30:00Z",
  "level": "INFO",
  "service": "zenmei-mei-api",
  "message": "Request processed",
  "trace_id": "abc123",
  "user_id": "user-456"
}
```

---

## 🚀 Deploy

### Local (Docker Compose)

```bash
./start.sh
```

### Google Cloud Run

```bash
# Via GitHub Actions (automático no push para main)
git push origin main

# Manual
for api in zenmei-*-api; do
    gcloud run deploy $api \
        --source ./$api \
        --platform managed \
        --region us-central1
done
```

---

## 📝 Comandos Úteis

### Docker Compose

```bash
# Iniciar tudo
docker-compose up -d

# Ver logs
docker-compose logs -f

# Ver logs de um serviço
docker-compose logs -f zenmei-mei-api

# Parar tudo
docker-compose down

# Rebuild e restart
docker-compose up -d --build

# Limpar volumes
docker-compose down -v
```

### Maven

```bash
# Build
mvn clean install

# Testes
mvn test

# Skip tests
mvn clean install -DskipTests

# SonarQube
mvn sonar:sonar -Dsonar.token=$SONAR_TOKEN
```

### Frontend

```bash
# Desenvolvimento
npm run dev

# Build
npm run build

# Preview
npm run preview

# Testes
npm run test
```

---

## 📚 Documentação

- [Documentação de API (Swagger)](http://localhost:8091/swagger-ui.html)
- [SonarQube Analysis](https://sonarcloud.io/organizations/zenmei)
- [Guia de Desenvolvimento](./DEVELOPMENT.md)
- [Arquitetura](./ARCHITECTURE.md)

---

## 🤝 Contribuindo

1. Fork o projeto
2. Crie uma branch (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

---

## 📊 Métricas

- **11** Microsserviços
- **408** Testes (85%+ coverage)
- **74** Arquivos de teste
- **33** Arquivos de segurança
- **~5.500** Linhas de código de teste

---

## 📄 Licença

Este projeto é proprietário e confidencial.

---

## 👥 Time

**Desenvolvido por:** JamesCoder - The Man in the Machine 🤖

---

## 🎯 Roadmap

- [x] Microsserviços base
- [x] Firebase Authentication
- [x] Testes (85%+)
- [x] SonarQube
- [x] Docker Compose
- [x] CI/CD Pipeline
- [ ] API Gateway (Spring Cloud Gateway)
- [ ] Service Discovery (Eureka)
- [ ] Distributed Tracing (Zipkin)
- [ ] Kubernetes deployment
- [ ] Grafana dashboards

---

**Status:** 🟢 **PRODUCTION READY**

**11 Microsserviços | 85% Coverage | Firebase Security | CI/CD Ready** ✅
