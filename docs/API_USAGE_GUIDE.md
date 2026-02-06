# 📖 Guia de Uso da API - ZenMei BFF

## 🎯 Visão Geral

Este guia contém exemplos práticos de como usar a API do ZenMei BFF para gerenciar MEIs e suas obrigações fiscais.

**Base URL (BFF):** `http://localhost:8081`  
**Base URL (Backend):** `http://localhost:8080`

---

## 🔐 Autenticação

Todas as requisições devem incluir o token JWT do Firebase no header:

```http
Authorization: Bearer <JWT_TOKEN>
X-User-Id: <UUID_DO_USUARIO>
```

---

## 📋 Endpoints - MEI Management

### 1. Listar Todos os MEIs

```http
GET /api/bff/v1/mei
Headers:
  Authorization: Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6...
  X-User-Id: 550e8400-e29b-41d4-a716-446655440000
```

**Response (200 OK):**
```json
[
  {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "nome": "João Silva",
    "nomeFantasia": "JS Soluções",
    "cpf": "12345678901",
    "cnpj": "12345678000190",
    "email": "joao@exemplo.com",
    "telefone": "(11) 98765-4321",
    "dataNascimento": "1985-05-15",
    "dataAbertura": "2020-01-10"
  }
]
```

---

### 2. Buscar MEI por ID

```http
GET /api/bff/v1/mei/123e4567-e89b-12d3-a456-426614174000
```

**Response (200 OK):**
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "nome": "João Silva",
  "nomeFantasia": "JS Soluções",
  "cpf": "12345678901",
  "cnpj": "12345678000190",
  "email": "joao@exemplo.com",
  "telefone": "(11) 98765-4321",
  "endereco": {
    "logradouro": "Rua das Flores",
    "numero": "123",
    "complemento": "Sala 1",
    "bairro": "Centro",
    "cidade": "São Paulo",
    "estado": "SP",
    "cep": "01234-567"
  },
  "dataNascimento": "1985-05-15",
  "dataAbertura": "2020-01-10",
  "ativo": true
}
```

**Response (404 Not Found):**
```json
{
  "timestamp": "2026-01-23T15:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "MEI não encontrado",
  "path": "/api/bff/v1/mei/123e4567-e89b-12d3-a456-426614174000"
}
```

---

### 3. Buscar MEI por Email

```http
GET /api/bff/v1/mei/email/joao@exemplo.com
```

**Response:** Igual ao endpoint de busca por ID

---

### 4. Buscar MEI por CPF

```http
GET /api/bff/v1/mei/cpf/12345678901
```

**Response:** Igual ao endpoint de busca por ID

---

### 5. Buscar MEI por CNPJ

```http
GET /api/bff/v1/mei/cnpj/12345678000190
```

**Response:** Igual ao endpoint de busca por ID

---

### 6. Criar Novo MEI

```http
POST /api/bff/v1/mei
Content-Type: application/json

{
  "nome": "Maria Santos",
  "nomeFantasia": "Maria Doces",
  "cpf": "98765432109",
  "cnpj": "98765432000111",
  "email": "maria@exemplo.com",
  "telefone": "(11) 91234-5678",
  "dataNascimento": "1990-03-20",
  "dataAbertura": "2023-06-15",
  "endereco": {
    "logradouro": "Av. Paulista",
    "numero": "1000",
    "bairro": "Bela Vista",
    "cidade": "São Paulo",
    "estado": "SP",
    "cep": "01310-100"
  },
  "ativo": true
}
```

**Response (201 Created):**
```json
{
  "id": "456e7890-e89b-12d3-a456-426614174001",
  "nome": "Maria Santos",
  "nomeFantasia": "Maria Doces",
  "cpf": "98765432109",
  "cnpj": "98765432000111",
  "email": "maria@exemplo.com",
  "telefone": "(11) 91234-5678",
  "dataNascimento": "1990-03-20",
  "dataAbertura": "2023-06-15",
  "ativo": true
}
```

---

### 7. Atualizar MEI

```http
PUT /api/bff/v1/mei/123e4567-e89b-12d3-a456-426614174000
Content-Type: application/json

{
  "nome": "João Silva Santos",
  "nomeFantasia": "JS Soluções Premium",
  "email": "joao.silva@exemplo.com",
  "telefone": "(11) 98888-9999"
}
```

**Response (200 OK):**
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "nome": "João Silva Santos",
  "nomeFantasia": "JS Soluções Premium",
  "email": "joao.silva@exemplo.com",
  "telefone": "(11) 98888-9999",
  "cpf": "12345678901",
  "cnpj": "12345678000190",
  "ativo": true
}
```

---

### 8. Deletar MEI

```http
DELETE /api/bff/v1/mei/123e4567-e89b-12d3-a456-426614174000
```

**Response (204 No Content):**
```
(sem corpo de resposta)
```

---

## 💼 Endpoints - Obrigações Fiscais

### 1. Listar Tipos de Obrigações Fiscais

```http
GET /api/bff/v1/mei/obrigacoes-fiscais/tipos
```

**Response (200 OK):**
```json
[
  {
    "id": "a1b2c3d4-e5f6-7890-a1b2-c3d4e5f67890",
    "obrigacao": "DAS Mensal",
    "mesCompetencia": "01",
    "diaCompetencia": "20"
  },
  {
    "id": "b2c3d4e5-f6a7-8901-b2c3-d4e5f6a78901",
    "obrigacao": "DASN-SIMEI",
    "mesCompetencia": "12",
    "diaCompetencia": "31"
  },
  {
    "id": "c3d4e5f6-a7b8-9012-c3d4-e5f6a7b89012",
    "obrigacao": "Relatório de Receitas",
    "mesCompetencia": "01",
    "diaCompetencia": "10"
  }
]
```

---

### 2. Listar Obrigações de um MEI

```http
GET /api/bff/v1/mei/123e4567-e89b-12d3-a456-426614174000/obrigacoes-fiscais
```

**Response (200 OK):**
```json
[
  {
    "id": "obr1-uuid",
    "idMei": "123e4567-e89b-12d3-a456-426614174000",
    "idObrigacao": "a1b2c3d4-e5f6-7890-a1b2-c3d4e5f67890",
    "obrigacao": "DAS Mensal",
    "diaCompetencia": "20",
    "mesAnoCompetencia": "01/2026",
    "status": "A vencer",
    "pdfRelatorio": null
  },
  {
    "id": "obr2-uuid",
    "idMei": "123e4567-e89b-12d3-a456-426614174000",
    "idObrigacao": "b2c3d4e5-f6a7-8901-b2c3-d4e5f6a78901",
    "obrigacao": "DASN-SIMEI",
    "diaCompetencia": "31",
    "mesAnoCompetencia": "12/2025",
    "status": "Em dia",
    "pdfRelatorio": "https://exemplo.com/dasn-2025.pdf"
  },
  {
    "id": "obr3-uuid",
    "idMei": "123e4567-e89b-12d3-a456-426614174000",
    "idObrigacao": "c3d4e5f6-a7b8-9012-c3d4-e5f6a7b89012",
    "obrigacao": "Relatório de Receitas",
    "diaCompetencia": "10",
    "mesAnoCompetencia": "01/2026",
    "status": "A vencer",
    "pdfRelatorio": null
  }
]
```

**Observação:** O BFF garante que sempre retorna exatamente 3 obrigações, com DASN-SIMEI incluído.

---

### 3. Listar MEIs com Obrigações Atrasadas

```http
GET /api/bff/v1/mei/123e4567-e89b-12d3-a456-426614174000/obrigacoes-atrasadas
```

**Response (200 OK):**
```json
[
  {
    "idMei": "123e4567-e89b-12d3-a456-426614174000",
    "nomeMei": "JS Soluções",
    "quantidadeAtrasadas": 2
  },
  {
    "idMei": "456e7890-e89b-12d3-a456-426614174001",
    "nomeMei": "Maria Doces",
    "quantidadeAtrasadas": 1
  }
]
```

---

### 4. Fechar/Concluir Obrigação Fiscal

```http
POST /api/bff/v1/mei/123e4567-e89b-12d3-a456-426614174000/obrigacoes-fiscais/obr1-uuid/fechar
```

**Response (200 OK):**
```json
{
  "id": "obr1-uuid",
  "idMei": "123e4567-e89b-12d3-a456-426614174000",
  "idObrigacao": "a1b2c3d4-e5f6-7890-a1b2-c3d4e5f67890",
  "obrigacao": "DAS Mensal",
  "diaCompetencia": "20",
  "mesAnoCompetencia": "01/2026",
  "status": "CONCLUIDA",
  "pdfRelatorio": null
}
```

**Observação:** Após fechar uma obrigação, o sistema automaticamente cria a próxima obrigação do mesmo tipo para o próximo período.

---

## 🔧 Tratamento de Erros

### Circuit Breaker Ativo

Quando o backend está indisponível e o Circuit Breaker é ativado:

```json
{
  "timestamp": "2026-01-23T15:30:00",
  "status": 503,
  "error": "Service Unavailable",
  "message": "Service temporarily unavailable. Please try again later.",
  "path": "/api/bff/v1/mei/123e4567-e89b-12d3-a456-426614174000/obrigacoes-fiscais/obr1-uuid/fechar"
}
```

### Validação de Dados

```json
{
  "timestamp": "2026-01-23T15:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": [
    {
      "field": "email",
      "message": "Email inválido"
    },
    {
      "field": "cpf",
      "message": "CPF deve ter 11 dígitos"
    }
  ]
}
```

---

## 🧪 Exemplos com cURL

### Listar MEIs
```bash
curl -X GET \
  http://localhost:8081/api/bff/v1/mei \
  -H 'Authorization: Bearer YOUR_JWT_TOKEN' \
  -H 'X-User-Id: 550e8400-e29b-41d4-a716-446655440000'
```

### Criar MEI
```bash
curl -X POST \
  http://localhost:8081/api/bff/v1/mei \
  -H 'Content-Type: application/json' \
  -H 'Authorization: Bearer YOUR_JWT_TOKEN' \
  -d '{
    "nome": "Maria Santos",
    "nomeFantasia": "Maria Doces",
    "cpf": "98765432109",
    "cnpj": "98765432000111",
    "email": "maria@exemplo.com",
    "telefone": "(11) 91234-5678"
  }'
```

### Fechar Obrigação
```bash
curl -X POST \
  http://localhost:8081/api/bff/v1/mei/123e4567-e89b-12d3-a456-426614174000/obrigacoes-fiscais/obr1-uuid/fechar \
  -H 'Authorization: Bearer YOUR_JWT_TOKEN'
```

---

## 📊 Status Codes

| Código | Significado | Quando Ocorre |
|--------|-------------|---------------|
| 200 | OK | Requisição bem-sucedida (GET, PUT, POST com resposta) |
| 201 | Created | Recurso criado com sucesso (POST) |
| 204 | No Content | Recurso deletado com sucesso (DELETE) |
| 400 | Bad Request | Dados inválidos na requisição |
| 401 | Unauthorized | Token JWT inválido ou ausente |
| 403 | Forbidden | Usuário sem permissão |
| 404 | Not Found | Recurso não encontrado |
| 500 | Internal Server Error | Erro interno no servidor |
| 503 | Service Unavailable | Serviço temporariamente indisponível (Circuit Breaker) |

---

## 🎓 Melhores Práticas

### 1. Sempre use HTTPS em produção
```
https://api.zenmei.com.br/api/bff/v1/mei
```

### 2. Armazene tokens de forma segura
- Nunca exponha tokens no código
- Use variáveis de ambiente
- Implemente refresh token

### 3. Trate Circuit Breaker adequadamente
```javascript
try {
  const response = await fetch('/api/bff/v1/mei');
  if (response.status === 503) {
    // Mostrar mensagem amigável ao usuário
    showMessage('Serviço temporariamente indisponível. Tente novamente em alguns minutos.');
  }
} catch (error) {
  // Tratamento de erro
}
```

### 4. Implemente retry no frontend
```javascript
async function fetchWithRetry(url, options, retries = 3) {
  for (let i = 0; i < retries; i++) {
    try {
      const response = await fetch(url, options);
      if (response.ok) return response;
      if (response.status === 503 && i < retries - 1) {
        await new Promise(resolve => setTimeout(resolve, 2000 * (i + 1)));
        continue;
      }
      throw new Error(`HTTP ${response.status}`);
    } catch (error) {
      if (i === retries - 1) throw error;
    }
  }
}
```

---

## 📝 Notas Importantes

1. **Paginação:** Atualmente não implementada. Recomenda-se adicionar para listagens grandes.
2. **Rate Limiting:** Ainda não configurado. Planejar implementação futura.
3. **Cache:** Considerar implementar cache para consultas frequentes (tipos de obrigações).
4. **Versionamento:** API está na v1. Planejar v2 para breaking changes futuros.

---

**Documento criado por:** JamesCoder  
**Data:** 23/01/2026  
**Versão da API:** v1  
**Status:** ✅ Completo e Validado
